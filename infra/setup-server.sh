#!/bin/bash

SELECTED_OPTION

createInternalNetwork() {
  echo "####### Please insert the private network name #######"
  printf "Input: "
  read -r PRIVATE_NETWORK
	if [ -z "$PRIVATE_NETWORK" ]; then
	  echo "Skipping private network creation because input is empty."
	  return 0
	fi
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
	echo "####### Please insert the public network name #######"
  printf "Input: "
  read -r PUBLIC_NETWORK
	if [ -z "$PUBLIC_NETWORK" ]; then
    echo "Skipping public network creation because input is empty."
    return 0
  fi
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
  echo "####### Please insert the database volume name #######"
  printf "Input: "
  read -r DATABASE_VOLUME_NAME
  if [ -z "$DATABASE_VOLUME_NAME" ]; then
    echo "Skipping volume creation because input is empty."
    return 0
  fi
  local VOLUME_NAME_MATCH_COUNT=$(docker volume ls | grep -c "$DATABASE_VOLUME_NAME")
  if [ "$VOLUME_NAME_MATCH_COUNT" -eq 0 ]; then
    echo "Creating volume $DATABASE_VOLUME_NAME"
    docker volume create "$DATABASE_VOLUME_NAME"
  else
    echo "This volume already existed and the creation was skipped"
  fi
}

createDatabaseContainer() {
	local NETWORK=$1
	local DATABASE_USER=$2
	local DATABASE_PASSWORD=$3
	local VOLUME_NAME=$4

	echo "Network: $NETWORK"
	echo "User: $DATABASE_USER"
	echo "Password: $DATABASE_PASSWORD"
	echo "volume: $VOLUME_NAME"

	docker run --name fin_tool_db-script --restart always --network "$NETWORK" \
	  --volume "$VOLUME_NAME:/var/lib/db" -p "5432:5432" \
	  -e POSTGRES_PASSWORD="$DATABASE_PASSWORD" -e POSTGRES_USER="$DATABASE_USER" \
	  -e POSTGRES_DB="FINANCIAL_TOOL_DB" -d postgres
}

checkOperationResult() {
	if [ $? -eq 0 ]; then
  	echo "$1"
	else
		echo "$2"
	fi
}

createDatabase() {
  echo "####### Please insert the follow info #######"
  printf "Database user: "
  read -r DATABASE_USER
  printf "Database password: "
  read -rs DATABASE_PASSWORD
  printf "\nNetworks (split by comma): "
  read -r DATABASE_NETWORKS
  printf "Database volume name: "
  read -r DATABASE_VOLUME
  createDatabaseContainer "$DATABASE_NETWORKS" "$DATABASE_USER" "$DATABASE_PASSWORD" "$DATABASE_VOLUME"
}

showMenu() {
  echo "####### Welcome! #######"
  echo "####### Please select an option #######"
  echo "0- Exit."
  echo "1- Create private network."
  echo "2- Create private public."
  echo "3- Create database volume."
  echo "4- Create database."
  printf "Selected option: "
  read -r SELECTED_OPTION
}

while [ -z "$SELECTED_OPTION" ] || [ "$SELECTED_OPTION" -ne 0 ];
do
  showMenu
  case "$SELECTED_OPTION" in
    1) createInternalNetwork;;
    2) createPublicNetwork;;
    3) createDatabaseVolume;;
    4) createDatabase;;
  esac
done
echo "Server setup completed"