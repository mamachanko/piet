#!/usr/bin/env bash

set -euxo pipefail

cd "$(dirname "$0")"/../ci

CONCOURSE_PORT=9000

function main() {
  get_concourse
  configure_concourse
  start_concourse
  install_fly_cli

  set +x
  echo
  echo "CI is up and running. 🛫"
  echo "Happy shipping! 🚢"
  echo
}

function get_concourse() {
  if [[ ! -d concourse-docker ]]; then
    git clone \
      https://github.com/concourse/concourse-docker
  fi
}

function configure_concourse() {
  pushd concourse-docker
  ./keys/generate
  sed -i tmp \
    -e "s/ports: \[\"8080:8080\"\]/ports: \[\"${CONCOURSE_PORT}:8080\"\]/g" \
    -e "s/CONCOURSE_EXTERNAL_URL: http:\/\/localhost:8080/CONCOURSE_EXTERNAL_URL: http:\/\/localhost:${CONCOURSE_PORT}/g" \
    docker-compose.yml
  popd
}

function start_concourse() {
  pushd concourse-docker
  docker-compose down
  docker-compose up -d
  popd
  while ! curl -XOPTIONS "http://localhost:${CONCOURSE_PORT}"; do
    echo 'concourse is not yet ready'
    sleep 1
  done
}

function install_fly_cli() {
  curl "http://localhost:${CONCOURSE_PORT}/api/v1/cli?arch=amd64&platform=darwin" >/usr/local/bin/fly
  chmod a+x /usr/local/bin/fly
}

main
