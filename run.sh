#!/usr/bin/env sh
# Build (if needed) and start the live demo. Extra args are passed through, e.g. ./run.sh --reset
set -e
cd "$(dirname "$0")"
[ -f target/strategy-demo.jar ] || mvn -q package
exec java -jar target/strategy-demo.jar "$@"
