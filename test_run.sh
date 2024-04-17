#!/bin/bash

if [ "$1" == "docker" ]; then
    ENV_ARG="-Dtest.env=docker"
else
    ENV_ARG=""
fi

./gradlew :rococo-e-2-e-tests:clean :rococo-e-2-e-tests:test $ENV_ARG
./gradlew :rococo-e-2-e-tests:allureServe
