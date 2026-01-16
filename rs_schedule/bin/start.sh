#!/usr/bin/env bash

PRG="$0"
PRGDIR=`dirname "$PRG"`
SCHEDULER_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

PID_FILE=$SCHEDULER_HOME/bin/scheduler.pid

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
    nohup $RUN_JAVA -DDamoaScheduler $JAVA_OPTS -Dlogging.config=file:$SCHEDULER_HOME/config/logback.xml -Dspring.config.location=$SCHEDULER_HOME/config/ -jar $SCHEDULER_HOME/lib/scheduler-0.0.1-SNAPSHOT.jar 1>/dev/null 2>&1 &
    echo $! > ${PID_FILE}
else
    echo "Another scheduler instance is already started in this folder."
    exit 0
fi
