#!/usr/bin/env bash

set -euxo pipefail

cd "$(dirname "$0")"/..

main() {
  compile
  package
}

compile() {
  ./gradlew \
    --no-daemon \
    clean \
    build
}

package() {
  mkdir -p "../out"

  cp \
    build/libs/piet-0.0.1-SNAPSHOT.jar \
    ../out/piet.jar
  cp \
    ci/manifest.yml \
    ../out/

  VERSION=$(git rev-parse --short HEAD)
  pushd ../out
  tar \
    -czvf \
    "piet-$VERSION.tar.gz" \
    piet.jar \
    manifest.yml
  popd

}

main
