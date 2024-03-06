#!/bin/bash

findBackendCurrentContainerAndDelete() {
  echo "Searching current backend container..."
  CURRENT_BACKEND_CONTAINER=($(docker ps -a | grep backend-api* | awk '{ print $1 }' | tail -n +1))
  if [ -n "$CURRENT_BACKEND_CONTAINER" ]; then
    echo "Removing current backend container..."
    if [[ "$(docker rm -f "$CURRENT_BACKEND_CONTAINER")" -eq 0 ]]; then
      echo "Oldest backend container removed successfully"
    else
      echo "Error to remove oldest backend container"
    fi
  fi
}

runFlyway() {
  echo "Checking existent migrations..."
  cd
  echo "Cloning repository..."
  git clone https://github.com/ThonAtaide/financial-tool-api.git
  cd financial-tool-api/src/main/resources/db/
  MIGRATIONS_PATH=$(pwd)
  echo "validating migrations repository..."
  docker run --network="$PRIVATE_NETWORK_NAME" --rm -v "$MIGRATIONS_PATH":/flyway/sql flyway/flyway \
  -url="jdbc:postgresql://$DATABASE_CONTAINER_NAME:5432/$DATABASE_NAME" -user="$DATABASE_USER" -password="$DATABASE_PASSWORD" -postgresql.transactional.lock=false migrate
  cd
  sudo rm -r financial-tool-api/
  echo "Flyway step concluded"
}

createBackendContainerAndDeploy() {
  echo "creating backend container..."
  docker image rm ataide/financial-tool-api
  docker run --name backend-api --restart always -itd -p 8080:8080 \
  --network="$PRIVATE_NETWORK_NAME" -e DATABASE_HOST="$DATABASE_CONTAINER_NAME" \
   -e DATABASE_NAME="$DATABASE_NAME" -e DATABASE_USERNAME="$DATABASE_USER" \
   -e DATABASE_PASSWORD="$DATABASE_PASSWORD" -e FLYWAY_USERNAME="$DATABASE_USER" \
   -e FLYWAY_PASSWORD="$DATABASE_PASSWORD" -e ALLOWED_EMAIL_LIST="$ALLOWED_EMAIL_LIST" \
   -e SECRET-KEY="$SECRET_KEY" \
    ataide/financial-tool-api

}

start_nginx_proxy() {
  docker start "$NGINX_CONTAINER"
}

findBackendCurrentContainerAndDelete
runFlyway
createBackendContainerAndDeploy
start_nginx_proxy