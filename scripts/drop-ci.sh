#!/usr/bin/env bash

set -euxo pipefail

cd "$(dirname "$0")"/../ci

docker-compose \
  down \
  --volumes \
  --remove-orphans

if [[ -d concourse-docker ]]; then
  rm -rf concourse-docker
fi
