#!/usr/bin/env bash

set -euxo pipefail

cd "$(dirname "$0")/.."

main() {
  install_db
  start_db
}

install_db() {
  apt-get update
  apt-get install \
    -y \
    gnupg2 \
    lsb-release

  wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc |
    apt-key add -
  echo "deb http://apt.postgresql.org/pub/repos/apt/ $(lsb_release -cs)-pgdg main" |
    tee /etc/apt/sources.list.d/pgdg.list

  apt-get update
  apt-get install \
    -y \
    postgresql-12 \
    postgresql-client-12

  echo "host all all 127.0.0.1/32 trust" >/etc/postgresql/12/main/pg_hba.conf
}

start_db() {
  /etc/init.d/postgresql start
}

main
