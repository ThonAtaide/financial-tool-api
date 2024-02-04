#!/bin/bash

createInternalNetwork() {
	local PRIVATE_NETWORK=$1
	local NETWORK_NAME_MATCH_COUNT=`docker network ls | grep -c $PRIVATE_NETWORK`

	if [ $NETWORK_NAME_MATCH_COUNT == 0 ]; then
	echo "Creating private network..."
	docker network create -d bridge --internal $PRIVATE_NETWORK
	checkOperationResult "Network created successfully" "Network created failed"
	else
		echo "The network $PRIVATE_NETWORK already exists and could not be created."
	fi
}

createPublicNetwork() {
	local PUBLIC_NETWORK=$1
	local NETWORK_NAME_MATCH_COUNT=`docker network ls | grep -c $PUBLIC_NETWORK`
	if [ $NETWORK_NAME_MATCH_COUNT -eq 0 ]; then
		echo "Creating public network..."
		docker network create -d bridge $PUBLIC_NETWORK
		checkOperationResult "Network created successfully" "Network created failed"
	else
		echo "The network $PUBLIC_NETWORK already exists and could not be created."
	fi
}

checkOperationResult() {
	if [ $? -eq 0 ]; then
  	echo "$1"
	else
		echo "$2"
	fi
}



echo "####### Welcome! #######"

echo "Please insert the private network name: " 
read PRIVATE_NETWORK
createInternalNetwork $PRIVATE_NETWORK

echo "Please insert the public network name: "
read PUBLIC_NETWORK
createPublicNetwork $PUBLIC_NETWORK

