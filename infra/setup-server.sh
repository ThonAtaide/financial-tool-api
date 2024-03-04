#!/bin/bash

export $(cat components-name.env | egrep -v "(^#.*|^$)" | xargs)
export $(cat containers.env | egrep -v "(^#.*|^$)" | xargs)
./network-script.sh
./volumes-script.sh
./deploy-db.sh
./deploy-backend.sh