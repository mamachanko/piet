#!/usr/bin/env bash

set -euxo pipefail

cd "$(dirname "$0")"/../ci

CONCOURSE_PORT=9000

function main() {
  get_concourse_docker
  configure_concourse
  start_concourse
  install_fly_cli
  deploy_pipeline

  set +x
  echo
  echo "CI is up and running. 🛫"
  echo "Happy shipping! 🚢"
  echo
}

function get_concourse_docker() {
  if [[ ! -d concourse-docker ]]; then
    git clone \
      https://github.com/concourse/concourse-docker
  fi
}

function configure_concourse() {
  pushd concourse-docker
  ./keys/generate
  popd
}

function start_concourse() {
  docker-compose \
    down \
    --volumes \
    --remove-orphans

  MINIO_ACCESS_KEY=$(yq r secrets.yml minio-access-key) \
  MINIO_SECRET_KEY=$(yq r secrets.yml minio-secret-key) \
  docker-compose \
    up \
    --detach

  while ! curl -XOPTIONS "http://localhost:${CONCOURSE_PORT}"; do
    echo 'concourse is not yet ready'
    sleep 1
  done
}

function install_fly_cli() {
  curl "http://localhost:${CONCOURSE_PORT}/api/v1/cli?arch=amd64&platform=darwin" >/usr/local/bin/fly
  chmod a+x /usr/local/bin/fly
}

function deploy_pipeline() {
  ../scripts/deploy-pipeline.sh

  fly \
    login \
    --concourse-url "http://localhost:${CONCOURSE_PORT}" \
    --username test \
    --password test \
    --target piet-ci

  fly \
    --target piet-ci \
    unpause-pipeline \
    --pipeline piet
}

main
