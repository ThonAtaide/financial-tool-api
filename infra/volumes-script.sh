#!/bin/bash

DATABASE_VOLUME_NAME="database_volume"
NGINX_PROXY_CONFIG_VOLUME_NAME="nginx_ssl_config_volume"
NGINX_SSL_CONFIG_VOLUME_NAME="nginx_ssl_config_volume"
NGINX_SSL_CONFIG_VOLUME_NAME="nginx_ssl_certificates_volume"

create_volume() {
  local VOLUME_NAME=$1
  local VOLUME_NAME_MATCH_COUNT=$(docker volume ls | grep -c "$VOLUME_NAME")
    if [ "$VOLUME_NAME_MATCH_COUNT" -eq 0 ]; then
      echo "Creating volume $VOLUME_NAME"
      docker volume create "$VOLUME_NAME"
    else
      echo "Volume $VOLUME_NAME already existed and the creation was skipped"
    fi
}

setup_ssl() {
  docker rm temp-container
  docker container run --name temp-container -itd -v "$NGINX_SSL_CONFIG_VOLUME_NAME":/etc/ssl-conf -v "$NGINX_SSL_CONFIG_VOLUME_NAME":/etc/certificates nginx
  setup_ssl_config
  setup_ssl_certificates
  setup_reverse_proxy
  docker rm -f temp-container
}

setup_ssl_config() {
  if [ "$(docker exec -it temp-container ls /etc/ | grep -c ssl-conf)" == 0 ] || [ "$(docker exec -it temp-container ls /etc/ssl-conf)" == 0 ]; then
    echo "Configuring ssl config files..."
    mkdir ~/ssl-conf
    curl -s https://raw.githubusercontent.com/certbot/certbot/master/certbot-nginx/certbot_nginx/_internal/tls_configs/options-ssl-nginx.conf > "~/ssl-conf/options-ssl-nginx.conf"
    curl -s https://raw.githubusercontent.com/certbot/certbot/master/certbot/certbot/ssl-dhparams.pem > "~/ssl-conf/ssl-dhparams.pem"
    docker cp ~/ssl-conf temp-container:/etc/
    echo "SSL config files copied to the volume."
    echo "Removing directory ssl-conf"
    rm -r ~/ssl-conf
  else
      echo "SSL config files already exists."
  fi
}

setup_ssl_certificates() {
  if [ "$(docker exec -it temp-container ls /etc/ | grep -c certificates)" == 0 ] || [ "$(docker exec -it temp-container ls /etc/certificates)" == 0 ]; then
    echo "Configuring ssl certificate files..."
    mkdir ~/certificates
    openssl req -x509 -newkey rsa:4096 -keyout ~/certificates/key.pem -out ~/certificates/cert.pem -sha256 -days 365 -nodes -subj "/C=XX/ST=São Paulo/L=São Carlos/O=Kathon/OU=CompanySectionName/CN=CommonNameOrHostname"
    docker cp ~/certificates temp-container:/etc/
    echo "SSL certificates copied to the volume."
    echo "Removing directory certificates"
    rm -r ~/certificates
  else
    echo "SSL certificates were already configured."
  fi
}

setup_reverse_proxy() {
    echo "copying file nginx.conf..."
    curl -s https://raw.githubusercontent.com/ThonAtaide/financial-tool-api/infra/infra/nginx.conf > "nginx.conf"
    echo "NGINX config file Downloaded..."
    docker cp nginx.conf temp-container:/etc/nginx/nginx.conf
    echo "Deleting nginx.conf..."
    rm nginx.conf
    echo "File nginx.conf deleted successfully..."
}

create_volume "$DATABASE_VOLUME_NAME"
create_volume "$NGINX_PROXY_CONFIG_VOLUME_NAME"
create_volume "$NGINX_SSL_CONFIG_VOLUME_NAME"
create_volume "$NGINX_SSL_CONFIG_VOLUME_NAME"
setup_ssl
