#!/usr/bin/env bash

set -euxo pipefail

cd "$(dirname "$0")"/..

DB_CONTAINER=$(docker-compose ps --quiet db)

docker \
  exec \
  --interactive \
  --tty \
  "$DB_CONTAINER" \
  psql --username postgres
