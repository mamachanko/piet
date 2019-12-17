#!/usr/bin/env bash

set -euxo pipefail

cd "$(dirname "$0")"/..

if [ -z ${PIET_BASEURI+x} ]; then
  set +x
  echo
  echo "⚠️  aborting. reason: env var PIET_BASEURI must be set."
  echo
  exit 1
fi

./scripts/post-image.sh \
  "$PIET_BASEURI" \
  $(pwd)/test-image.png \
  | grep -i "optical character recognition"

set +x
echo
echo "💨✅ Successfully smoke tested $PIET_BASEURI"
echo
