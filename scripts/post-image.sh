#!/usr/bin/env bash

set -euo pipefail

cd "$(dirname "$0")"

API_BASE_URI=${1:-http://localhost:8080}
IMAGE_CONTENT_URI=${2:-"$API_BASE_URI/test-image.png"}

function main() {
  IMAGE_URI=$(createImage)
  awaitComplete "${IMAGE_URI%$'\r'}"
}

function createImage() {
  curl \
    --silent \
    --include \
    --request POST \
    --data "{\"url\": \"${IMAGE_CONTENT_URI}\"}" \
    --header 'Content-Type: application/json' \
    --url "${API_BASE_URI}/api/images" |
    grep Location |
    sed 's/Location\: //g'
}

function awaitComplete() {
  local imageUri=$1
  local imageStatus="$(getStatus "$imageUri")"

  echo "Awaiting completion of image:"
  echo
  echo "  $imageUri"
  echo

  SECONDS=0
  TIMEOUT=60
  until [ "$imageStatus" = "Complete" ]; do

    if [ $SECONDS -ge $TIMEOUT ]; then
      echo "⏰ Awaiting completion timed out!"
      echo
      exit 1
    fi

    echo "⏳ status: $imageStatus"
    sleep 1
    imageStatus="$(getStatus "$imageUri")"
  done

  echo "✅ status: $imageStatus"
  echo
}

function getStatus() {
  local imageUri=$1

  curl \
    --silent \
    --request GET \
    --url "$imageUri" |
    jq --raw-output .status
}

main
