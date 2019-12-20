#!/usr/bin/env bash

set -euo pipefail

cd "$(dirname "$0")"/..

if [ -z ${PIET_BASE_URI+x} ]; then
  echo
  echo "⚠️  aborting. reason: env var PIET_BASE_URI must be set."
  echo
  exit 1
fi

TEST_IMAGE_CONTENT_URI="$PIET_BASE_URI/test-image.png"

echo "Smoke testing:"
echo
echo "  $PIET_BASE_URI"
echo

./scripts/post-image.sh \
  "$PIET_BASE_URI" \
  "$TEST_IMAGE_CONTENT_URI"

echo "Successfully smoke tested!"
echo
