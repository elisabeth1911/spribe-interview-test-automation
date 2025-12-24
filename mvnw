#!/bin/sh
# Apache Maven Wrapper
# https://maven.apache.org/wrapper/

set -euf

BASE_DIR=$(cd "$(dirname "$0")"; pwd -P)

WRAPPER_DIR="$BASE_DIR/.mvn/wrapper"
PROPERTIES_FILE="$WRAPPER_DIR/maven-wrapper.properties"
JAR_FILE="$WRAPPER_DIR/maven-wrapper.jar"

if [ ! -f "$PROPERTIES_FILE" ]; then
  echo "Missing $PROPERTIES_FILE" >&2
  exit 1
fi

if [ ! -f "$JAR_FILE" ]; then
  WRAPPER_URL=$(grep -E '^wrapperUrl=' "$PROPERTIES_FILE" | cut -d= -f2- || true)
  if [ -z "${WRAPPER_URL:-}" ]; then
    echo "Missing wrapperUrl in $PROPERTIES_FILE" >&2
    exit 1
  fi
  echo "Downloading Maven Wrapper jar..." >&2
  mkdir -p "$WRAPPER_DIR"
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$WRAPPER_URL" -o "$JAR_FILE"
  elif command -v wget >/dev/null 2>&1; then
    wget -q "$WRAPPER_URL" -O "$JAR_FILE"
  else
    echo "Neither curl nor wget is available to download $WRAPPER_URL" >&2
    exit 1
  fi
fi

JAVA_EXE="${JAVA_HOME:-}/bin/java"
if [ ! -x "$JAVA_EXE" ]; then
  JAVA_EXE=$(command -v java || true)
fi
if [ -z "${JAVA_EXE:-}" ]; then
  echo "Java executable not found (JAVA_HOME is not set and java is not on PATH)" >&2
  exit 1
fi

exec "$JAVA_EXE" -classpath "$JAR_FILE" -Dmaven.multiModuleProjectDirectory="$BASE_DIR" org.apache.maven.wrapper.MavenWrapperMain "$@"

