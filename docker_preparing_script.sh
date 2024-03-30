#!/bin/bash

echo "### Stop running docker-compose ###"
docker-compose down

echo "### Stop and remove  containers ###"
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)

services=("rococo-auth" "rococo-gateway" "rococo-userdata" "rococo-country" "rococo-museum" "rococo-artist")

for service in "${services[@]}"; do
  echo "### remove local image for $service###"
  docker rmi "koctuk999/$service"

  if [ "$1" = "push" ]; then
    echo "### Build & push images for $service ###"
    bash ./gradlew ":$service:jib" || exit 1
  else
    echo "### Build local images for $service ###"
    bash ./gradlew ":$service:jibDockerBuild" || exit 1
  fi
done

echo "### Start docker-compose ###"
docker-compose up -d
