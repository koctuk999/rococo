#!/bin/bash

source ./rococo-client/docker.properties

echo '### Build frontend image ###'
docker build --build-arg NPM_COMMAND=build:docker -t ${REGISTRY}/${NAME}:${VERSION} -t ${REGISTRY}/${NAME}:latest ./rococo-client || exit 1

echo '### Push frontend image ###'
docker push ${REGISTRY}/${NAME}:${VERSION}
docker push ${REGISTRY}/${NAME}:latest
