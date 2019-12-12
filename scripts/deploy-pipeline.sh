#!/usr/bin/env bash

set -euxo pipefail

cd "$(dirname "$0")"/../ci

fly \
  login \
  --concourse-url http://localhost:9000 \
  --username test \
  --password test \
  --target piet-ci

fly \
  --target piet-ci \
  set-pipeline \
  --non-interactive \
  --pipeline piet \
  --config pipeline.yml \
  --load-vars-from secrets.yml
