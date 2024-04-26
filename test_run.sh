#!/bin/bash

if [ "$1" == "docker" ]; then
    ENV_ARG="-Dtest.env=docker"
    TAG_ARG="$2"
else
    ENV_ARG=""
    TAG_ARG="$1"
fi

if [ -n "$TAG_ARG" ]; then
    TAG_ARG="-Ptag=$TAG_ARG"
else
    TAG_ARG=""
fi

./gradlew :rococo-e-2-e-tests:clean :rococo-e-2-e-tests:test $ENV_ARG $TAG_ARG
./gradlew :rococo-e-2-e-tests:allureServe
