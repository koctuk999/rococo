#!/bin/bash

echo "### Stop running docker-compose ###"
docker-compose down

if [ "$1" = "push" ]; then
  echo "### Build & push images ###"
  bash ./gradlew  :rococo-auth:jib || exit 1
  bash ./gradlew  :rococo-userdata:jib || exit 1
else
  echo "### Build local images ###"
  bash ./gradlew :rococo-auth:jibDockerBuild || exit 1
  bash ./gradlew :rococo-userdata:jibDockerBuild || exit 1
fi

echo "### Start docker-compose ###"
docker-compose up -d
