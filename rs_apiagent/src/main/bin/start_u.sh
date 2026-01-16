#!/bin/bash

export LANG=ko_KR.UTF-8

CLASSPATH=/home/damoa/app/server/apiagent:/home/damoa/app/server/apiagent/lib/ojdbc6.jar:/home/damoa/app/server/apiagent/lib/damoa-api-agent-1.0-SNAPSHOT.jar
export CLASSPATH

java -classpath $CLASSPATH com.damoa.DamoaClient