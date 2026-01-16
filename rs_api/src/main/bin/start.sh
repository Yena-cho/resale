#!/usr/bin/env bash

PRG="$0"
PRGDIR=`dirname "$PRG"`
API_SERVER_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

PID_FILE=$API_SERVER_HOME/bin/api-server.pid

if [ $JAVA_HOME ]
then
	echo "JAVA_HOME found at $JAVA_HOME"
	RUN_JAVA=$JAVA_HOME/bin/java
else
	echo "JAVA_HOME environment variable not available."
    RUN_JAVA=`which java 2>/dev/null`
fi

if [ -z $RUN_JAVA ]
then
    echo "JAVA could not be found in your system."
    echo "please install Java 1.6 or higher!!!"
    exit 1
fi

	echo "Path to Java : $RUN_JAVA"

#### you can enable following variables by uncommenting them

#### minimum heap size
MIN_HEAP_SIZE=1G

#### maximum heap size
MAX_HEAP_SIZE=1G

if [ "x$MIN_HEAP_SIZE" != "x" ]; then
	JAVA_OPTS="$JAVA_OPTS -Xms${MIN_HEAP_SIZE}"
fi

if [ "x$MAX_HEAP_SIZE" != "x" ]; then
	JAVA_OPTS="$JAVA_OPTS -Xmx${MAX_HEAP_SIZE}"
fi

echo "########################################"
echo "# RUN_JAVA=$RUN_JAVA"
echo "# JAVA_OPTS=$JAVA_OPTS"
echo "# starting now...."
echo "########################################"

PID=$(cat "${PID_FILE}");
if [ -z "${PID}" ]; then
    echo "Process id for scheduler instance is written to location: {$PID_FILE}"
    nohup $RUN_JAVA -DApiServer -Djava.security.egd=file:/dev/urandom $JAVA_OPTS -classpath $API_SERVER_HOME:$API_SERVER_HOME/config/* -jar $API_SERVER_HOME/lib/api-server-1.0-SNAPSHOT-jar-with-dependencies.jar 1>/dev/null 2>&1 &
#    nohup $RUN_JAVA -DApiServer $JAVA_OPTS -classpath ./:./config:$MY_CLASSPATH -Dlogging.config=file:$API_SERVER_HOME/config/logback.xml damoa.damoaSvr 1>/dev/null 2>&1 &
    echo $! > ${PID_FILE}
else
    echo "Another scheduler instance is already started in this folder."
    exit 0
fi
