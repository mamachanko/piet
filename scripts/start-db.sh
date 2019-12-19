#!/usr/bin/env bash

set -euxo pipefail

cd "$(dirname "$0")"/..

docker-compose up --detach
