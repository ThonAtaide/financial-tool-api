#!/bin/bash


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
	  --volume "$VOLUME_NAME:/var/lib/postgresql/data" -p "5432:5432" \
	  -e POSTGRES_PASSWORD="$DATABASE_PASSWORD" -e POSTGRES_USER="$DATABASE_USER" \
	  -e POSTGRES_DB="financial_tool_db" -d postgres
}

checkOperationResult() {
	if [ $? -eq 0 ]; then
  	echo "$1"
	else
		echo "$2"
	fi
}

createDatabase() {
  echo "##### Database creation menu #####"
  echo "##### Please select the network (splited by ',') #####"
  selectNetworkMenu
  echo "##### Please select the volume #####"
  selectVolumeMenu
  echo "####### Please insert the follow info #######"
  printf "Database user: "
  read -r DATABASE_USER
  printf "Database password: "
  read -rs DATABASE_PASSWORD
  printf "Networks (split by comma): "

  createDatabaseContainer "$SELECTED_NETWORK" "$DATABASE_USER" "$DATABASE_PASSWORD" "$SELECTED_VOLUME"
}

selectNetworkMenu() {
  NETWORKS=($(docker network ls | awk '{ print $2 }' | tail -n +1))
  NETWORKS_LENGTH=${!NETWORKS[@]}
  SELECTED_NETWORK_NUMBER=999999
  SELECTED_NETWORK=
  while [ -z "$SELECTED_NETWORK" ] ;
  do
    for index in $NETWORKS_LENGTH;
      do
        echo "$index - ${NETWORKS[$index]}"
      done
      printf "Please select the network number: "
      read -r SELECTED_NETWORK_NUMBER
      SELECTED_NETWORK="${NETWORKS[$SELECTED_NETWORK_NUMBER]}"
  done
}

selectVolumeMenu() {
  VOLUMES=($(docker volume ls | awk '{ print $2 }' | tail -n +1))
  VOLUMES_LENGTH=${!VOLUMES[@]}
  SELECTED_VOLUMES_NUMBER=999999
  SELECTED_VOLUME=
  while [ -z "$SELECTED_VOLUME" ] ;
  do
    for index in $VOLUMES_LENGTH;
      do
        echo "$index - ${VOLUMES[$index]}"
      done
      printf "Please select the volume number: "
      read -r SELECTED_VOLUMES_NUMBER
      SELECTED_VOLUME="${VOLUMES[SELECTED_VOLUMES_NUMBER]}"
  done
}

findBackendCurrentContainerAndDelete() {
  echo "Searching current backend container..."
  CURRENT_BACKEND_CONTAINER=($(docker ps -a | grep backend-api* | awk '{ print $1 }' | tail -n +1))
  if [ -n "$CURRENT_BACKEND_CONTAINER" ]; then
      echo "Removendo container do backend atual..."
      if [[ "$(docker rm -f "$CURRENT_BACKEND_CONTAINER")" -eq 0 ]]; then
        echo "Oldest backend container deleted successfully"
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
  -url="jdbc:postgresql://$DATABASE_HOST:5432/financial_tool_db" -user="$FLYWAY_DATABASE_USER" -password="$FLYWAY_DATABASE_PASSWORD" -postgresql.transactional.lock=false migrate
  cd
  sudo rm -r financial-tool-api/
  echo "Flyway step concluded"
}

collectBackendApplicationInfo() {
  local INFO_LABEL=$1
  printf '%s' "$INFO_LABEL"
  read -r DATABASE_INFO_INPUT
}

readBackendApplicationConfigs() {
  echo "##### Please select the database network #####"
  selectNetworkMenu
  PRIVATE_NETWORK_NAME=$SELECTED_NETWORK

  echo "##### Please select the public network #####"
  selectNetworkMenu
  PUBLIC_NETWORK_NAME=$SELECTED_NETWORK

  echo "##### Please provide database info #####"
  echo "$(docker ps)"
  collectBackendApplicationInfo "Database host: "
  DATABASE_HOST="$DATABASE_INFO_INPUT"

  collectBackendApplicationInfo "Application database user: "
  APPLICATION_DATABASE_USER="$DATABASE_INFO_INPUT"

  collectBackendApplicationInfo "Application database password: "
  APPLICATION_DATABASE_PASSWORD="$DATABASE_INFO_INPUT"

  collectBackendApplicationInfo "Flyway database user: "
  FLYWAY_DATABASE_USER=${DATABASE_INFO_INPUT:-$APPLICATION_DATABASE_USER}

  collectBackendApplicationInfo "Flyway database password: "
  FLYWAY_DATABASE_PASSWORD=${DATABASE_INFO_INPUT:-$APPLICATION_DATABASE_PASSWORD}

  collectBackendApplicationInfo "Secret key: "
  SECRET_KEY=$DATABASE_INFO_INPUT

  collectBackendApplicationInfo "Allowed emails: "
  ALLOWED_EMAIL_LIST=$DATABASE_INFO_INPUT

}

createBackendContainerAndDeploy() {
  echo "creating backend container..."
    docker create --name backend-api --restart always -p 8080:8080 \
    --network="$PRIVATE_NETWORK_NAME" -e DATABASE_HOST="$DATABASE_HOST" \
     -e DATABASE_NAME="financial_tool_db" -e DATABASE_USERNAME="$APPLICATION_DATABASE_USER" \
     -e DATABASE_PASSWORD="$APPLICATION_DATABASE_PASSWORD" -e FLYWAY_USERNAME="$FLYWAY_DATABASE_USER" \
     -e FLYWAY_PASSWORD="$FLYWAY_DATABASE_PASSWORD" -e ALLOWED_EMAIL_LIST="$ALLOWED_EMAIL_LIST" \
     -e SECRET-KEY="$SECRET_KEY" \
      ataide/financial-tool-api
  echo "Backend container created successfully."

  echo "Configuring container public network..."
  docker network connect "$PUBLIC_NETWORK_NAME" backend-api
  echo "Backend public network successfully configured."
  docker start backend-api
}

deployBackendApi() {
  readBackendApplicationConfigs
  findBackendCurrentContainerAndDelete
  runFlyway
  createBackendContainerAndDeploy
}

showMenu() {
  echo "####### Welcome! #######"
  echo "####### Please select an option #######"
  echo "0- Exit."
  echo "1- Create private network."
  echo "2- Create public network."
  echo "3- Create database volume."
  echo "4- Create database."
  echo "5- Deploy backend api."
  printf "Selected option: "
  read -r SELECTED_OPTION
}

#while [ -z "$SELECTED_OPTION" ] || [ -n "${SELECTED_OPTION//[0-9]/}" ] || [ "$SELECTED_OPTION" -ne 0 ];
#do
#  showMenu
#  case "$SELECTED_OPTION" in
#    1)
#      clear >$(tty)
#      createInternalNetwork;;
#    2)
#      clear >$(tty)
#      createPublicNetwork;;
#    3)
#      clear >$(tty)
#      createDatabaseVolume;;
#    4)
#      clear >$(tty)
#      createDatabase;;
#    5)
#      clear >$(tty)
#      deployBackendApi;;
#  esac
#done
#echo "Server setup completed"

export $(cat components-name.env | egrep -v "(^#.*|^$)" | xargs)
export $(cat containers.env | egrep -v "(^#.*|^$)" | xargs)
./network-script.sh
./volumes-script.sh
./deploy-db.sh
./deploy-backend.sh