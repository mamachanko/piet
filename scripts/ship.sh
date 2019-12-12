#!/usr/bin/env bash

set -euxo pipefail

cd "$(dirname "$0")"/..

./gradlew clean build

cf push

./scripts/post-image.sh https://piet.cfapps.io $(pwd)/test-image.png \
  | grep -i "optical character recognition"

git push

set +x
say successfully shipped Piet
echo
echo "🚢 successfully shipped Piet 🕵🏻‍♂️"
echo
