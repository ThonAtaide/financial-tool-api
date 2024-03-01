#!/bin/bash

PRIVATE_NETWORK_NAME="private_network"
PUBLIC_NETWORK_NAME="public_network"

checkOperationResult() {
	if [ "$?" -eq 0 ]; then
  	echo "$1"
	else
		echo "$2"
	fi
}

createInternalNetwork() {
	local NETWORK_NAME_MATCH_COUNT=$(docker network ls | grep -c "$PRIVATE_NETWORK_NAME")

	if [ "$NETWORK_NAME_MATCH_COUNT" == 0 ]; then
	  echo "Creating private network..."
	  docker network create -d bridge --internal "$PRIVATE_NETWORK_NAME"
	  checkOperationResult "Network created successfully" "Network created failed"
	else
		echo "Skipping private network creation cause it already exists."
	fi
}

createPublicNetwork() {
	local NETWORK_NAME_MATCH_COUNT=$(docker network ls | grep -c "$PUBLIC_NETWORK_NAME")
	if [ "$NETWORK_NAME_MATCH_COUNT" -eq 0 ]; then
		echo "Creating public network..."
		docker network create -d bridge "$PUBLIC_NETWORK_NAME"
		checkOperationResult "Network created successfully" "Network created failed"
	else
		echo "Skipping public creation cause it already exists."
	fi
}

createInternalNetwork
createPublicNetwork