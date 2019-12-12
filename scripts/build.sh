#!/usr/bin/env bash

set -euxo pipefail

cd "$(dirname "$0")"/..

./gradlew \
  --no-daemon \
  clean \
  build

mkdir -p ../out
cp \
  build/libs/piet-0.0.1-SNAPSHOT.jar \
  ../out/
