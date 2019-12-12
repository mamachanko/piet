#!/usr/bin/env bash

set -euxo pipefail

cd "$(dirname "$0")"

API_BASE_URI=${1:-http://localhost:8080}
IMAGE_PATH=${2:-../test-image.png}

curl \
  --silent \
  --include \
  --verbose \
  --request POST \
  --data-binary "@${IMAGE_PATH}" \
  --header 'Content-Type: image/png' \
  --url "${API_BASE_URI}/api/images"
