#!/usr/bin/env sh

set -euxo pipefail

cd "$(dirname "$0")"/..

main() {
  login
  create_bucket
  upload

  mc ls piet/piet
}

login() {
  mc \
    config \
    host \
    add \
    piet \
    "$MINIO_HOST" \
    "$MINIO_ACCESSKEY" \
    "$MINIO_SECRETKEY"
}

create_bucket() {
  mc \
    mb \
    --ignore-existing \
    piet/piet
}

upload() {
  mc \
    cp \
    ../out/piet-0.0.1-SNAPSHOT.jar \
    piet/piet
}

main
