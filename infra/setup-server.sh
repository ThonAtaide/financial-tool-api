#!/bin/bash

createInternalNetwork() {
	local PRIVATE_NETWORK=$1
	local NETWORK_NAME_MATCH_COUNT=$(docker network ls | grep -c "$PRIVATE_NETWORK")

	if [ "$NETWORK_NAME_MATCH_COUNT" == 0 ]; then
	echo "Creating private network..."
	docker network create -d bridge --internal "$PRIVATE_NETWORK"
	checkOperationResult "Network created successfully" "Network created failed"
	else
		echo "The network $PRIVATE_NETWORK already exists and could not be created."
	fi
}

createPublicNetwork() {
	local PUBLIC_NETWORK=$1
	local NETWORK_NAME_MATCH_COUNT=$(docker network ls | grep -c "$PUBLIC_NETWORK")
	if [ "$NETWORK_NAME_MATCH_COUNT" -eq 0 ]; then
		echo "Creating public network..."
		docker network create -d bridge "$PUBLIC_NETWORK"
		checkOperationResult "Network created successfully" "Network created failed"
	else
		echo "The network $PUBLIC_NETWORK already exists and could not be created."
	fi
}

createDatabaseVolume() {
  local DATABASE_VOLUME_NAME=$1
  local VOLUME_NAME_MATCH_COUNT=$(docker volume ls | grep -c "$DATABASE_VOLUME_NAME")
  if [ "$VOLUME_NAME_MATCH_COUNT" -eq 0 ]; then
    echo "Creating volume $DATABASE_VOLUME_NAME"
    docker volume create "$DATABASE_VOLUME_NAME"
  else
    echo "This volume already existed and the creation was skipped"
  fi
}

checkOperationResult() {
	if [ $? -eq 0 ]; then
  	echo "$1"
	else
		echo "$2"
	fi
}

deployDatabase() {
  echo "####### Please insert the follow info #######"
  printf "Container name: "
  read  -r DATABASE_CONTAINER_NAME
  printf "Database image and tag version: "
  read  -r DATABASE_IMAGE
  printf "Database port number: "
  read  -r DATABASE_PORT_NUMBER
  printf "Database exposed in port: "
  read  -r DATABASE_EXPOSED_PORT_NUMBER
  printf "Networks (split by comma): "
  read  -r DATABASE_NETWORKS
  printf "Mapped volume (db_volume:/var/lib/db) (split by comma): "
  read  -r DATABASE_VOLUMES
  printf "Networks (split by comma): "
  printf "Database environment variables (split by comma x=y,a=b): "
  read  -r DATABASE_ENV_VARIABLES
}

echo "####### Welcome! #######"

echo "####### Please insert the private network name #######"
printf "Input: "
read -r PRIVATE_NETWORK
createInternalNetwork "$PRIVATE_NETWORK"

echo "####### Please insert the public network name #######"
printf "Input: "
read -r PUBLIC_NETWORK
createPublicNetwork "$PUBLIC_NETWORK"

echo "####### Please insert the database volume name #######"
printf "Input: "
read -r DATABASE_VOLUME_NAME
createDatabaseVolume "$DATABASE_VOLUME_NAME"

echo "Server setup completed"