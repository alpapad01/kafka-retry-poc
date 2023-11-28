#!/bin/bash

# Load external config, if it exists. Config file could be a config map in OCP, or a volume in docker-compose
CONFIG_FILE=/app/configuration/app.env
if [ -f "$CONFIG_FILE" ]; then
    set -a
    source ${CONFIG_FILE}
    set +a
fi

# Load external, application specific config, if it exists. Config file could be a config map in OCP, or a volume in docker-compose
APP_CONFIG_FILE=/app/${APPLICATION}/app.env
if [ -f "$APP_CONFIG_FILE" ]; then
    set -a
    source ${APP_CONFIG_FILE}
    set +a
fi

# check if we are running in kubernetes or in a docker engine
if [ -z "$KUBERNETES_SERVICE_HOST" ] 
then
   # Some other container runtime like docker
   export PROFILE_TO_ADD="docker"
   export LOGBACK_CONFIG="/app/logback-docker.xml"
else
   # Running inside kubernetes
   export PROFILE_TO_ADD="ocp"
   export LOGBACK_CONFIG="/app/logback-ocp.xml"
fi

if [ -z "$SPRING_PROFILES" ] 
then
   export APP_PROFILES="${PROFILE_TO_ADD}"
else
   # If there is a user set spring profile. then use it
   export APP_PROFILES="${SPRING_PROFILES},${PROFILE_TO_ADD}"
fi

exec /app/run.sh $@