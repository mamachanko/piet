#!/usr/bin/env bash

set -euxo pipefail

cd "$(dirname "$0")"/..

./gradlew \
  --no-daemon \
  clean \
  build
