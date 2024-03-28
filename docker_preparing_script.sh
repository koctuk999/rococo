#!/bin/bash

echo "### Stop running docker-compose ###"
docker-compose down

echo "### Stop and remove  containers ###"
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)

if [ "$1" = "push" ]; then
  echo "### Build & push images ###"
  bash ./gradlew  :rococo-auth:jib || exit 1
  bash ./gradlew  :rococo-gateway:jib || exit 1
  bash ./gradlew  :rococo-userdata:jib || exit 1
  bash ./gradlew  :rococo-country:jib || exit 1
  bash ./gradlew  :rococo-museum:jib || exit 1
else
  echo "### Build local images ###"
  bash ./gradlew :rococo-auth:jibDockerBuild || exit 1
  bash ./gradlew :rococo-gateway:jibDockerBuild || exit 1
  bash ./gradlew :rococo-userdata:jibDockerBuild || exit 1
  bash ./gradlew :rococo-country:jibDockerBuild || exit 1
  bash ./gradlew :rococo-museum:jibDockerBuild || exit 1
fi

echo "### Start docker-compose ###"
docker-compose up -d
