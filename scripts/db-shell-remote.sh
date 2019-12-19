#!/usr/bin/env bash

set -euxo pipefail

cd "$(dirname "$0")"/..

DB_CONTAINER=$(docker-compose ps --quiet db)
DB_URI=$(
  cf env piet |
    grep 'postgres://' |
    sed 's/.*"uri": "\(.*\)".*/\1/'
)

docker \
  exec \
  --interactive \
  --tty \
  "$DB_CONTAINER" \
  psql "$DB_URI"
