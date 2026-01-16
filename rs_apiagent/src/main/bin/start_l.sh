#!/bin/bash

export LANG=ko_KR.UTF-8
export APP_HOME=/home/damoa/app/server/apiagent
PID_FILE=$APP_HOME/bin/api-agent.pid

CLASSPATH=$APP_HOME:$APP_HOME/lib/ojdbc6.jar:$APP_HOME/lib/damoa-api-agent-1.0-SNAPSHOT.jar
export CLASSPATH
PID=$(cat "${PID_FILE}");
if [ -z "${PID}" ]; then
    echo "Process id for scheduler instance is written to location: {$PID_FILE}"
    java -classpath $CLASSPATH com.damoa.DamoaClient &
    echo $! > ${PID_FILE}
else
    echo "Another scheduler instance is already started in this folder."
    exit 0
fi
