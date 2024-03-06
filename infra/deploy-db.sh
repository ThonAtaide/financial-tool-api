#!/bin/bash

createDatabaseContainer() {

	local existent_database=$(docker ps | grep -c "$DATABASE_CONTAINER_NAME")
	if [ "$existent_database" != 0 ]; then
	  echo "Database already up and running...skipping deploy"
	  exit 0
	else
	  echo "Removing possible stopped database containers..."
	  docker rm "$DATABASE_CONTAINER_NAME"
	  echo "Starting database creation..."
	fi
	echo "Network: $PRIVATE_NETWORK_NAME"
	echo "User: $DATABASE_USER"
	echo "volume: $DATABASE_VOLUME_NAME"

  echo "DB User: $DATABASE_USER"
  echo "DB Password: $DATABASE_PASSWORD"
	docker run --name "$DATABASE_CONTAINER_NAME" --restart always --network "$PRIVATE_NETWORK_NAME" \
	  --volume "$DATABASE_VOLUME_NAME:/var/lib/postgresql/data" -p "5432:5432" \
	  -e POSTGRES_PASSWORD="$DATABASE_PASSWORD" -e POSTGRES_USER="$DATABASE_USER" \
	  -e POSTGRES_DB="$DATABASE_NAME" -d postgres
}

createDatabaseContainer