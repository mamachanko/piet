#!/usr/bin/env bash

set -euo pipefail

cd "$(dirname "$0")"/..

if [ -z ${PIET_BASEURI+x} ]; then
  echo
  echo "⚠️  aborting. reason: env var PIET_BASEURI must be set."
  echo
  exit 1
fi

TEST_IMAGE="$(pwd)/src/test/resources/test-image.png"

if [ ! -f "$TEST_IMAGE" ]; then
  echo
  echo "⚠️  aborting. reason: test image $TEST_IMAGE does not exist."
  echo
  exit 1
fi

echo "Smoke testing:"
echo
echo "  $PIET_BASEURI"
echo

./scripts/post-image.sh \
  "$PIET_BASEURI" \
  "$TEST_IMAGE"

echo "Successfully smoke tested $PIET_BASEURI"
echo
