#!/usr/bin/env bash

set -euxo pipefail

cd "$(dirname "$0")"/..

./scripts/build.sh

git push

set +x
say successfully shipped Piet
echo
echo "🚢🕵🏻‍♂️ successfully shipped Piet"
echo
