#!/usr/bin/env bash

start() {
PRG="$0"
PRGDIR=`dirname "$PRG"`
MESSAGE_SERVER_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`
cd $MESSAGE_SERVER_HOME

PID_FILE=$MESSAGE_SERVER_HOME/bin/message-server.pid

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
    echo "$RUN_JAVA -DDamoaMessageServer $JAVA_OPTS -Dlogging.config=file:$MESSAGE_SERVER_HOME/config/logback.xml -Dspring.config.location=$MESSAGE_SERVER_HOME/config/application.yml -jar $MESSAGE_SERVER_HOME/lib/damoa-shinhan-va-agent.jar"
    nohup $RUN_JAVA -Djava.security.egd=file:/dev/urandom -DDamoaMessageServer $JAVA_OPTS -Dlogging.config=file:$MESSAGE_SERVER_HOME/config/logback.xml -Dspring.config.location=$MESSAGE_SERVER_HOME/config/application.yml -jar $MESSAGE_SERVER_HOME/lib/damoa-shinhan-va-agent.jar 1>/dev/null 2>&1 &
    echo $! > ${PID_FILE}
else
    echo "Another scheduler instance is already started in this folder."
    exit 0
fi
}

console() {
PRG="$0"
PRGDIR=`dirname "$PRG"`
MESSAGE_SERVER_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`
cd $MESSAGE_SERVER_HOME

PID_FILE=$MESSAGE_SERVER_HOME/bin/message-server.pid

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
    echo "$RUN_JAVA -DDamoaMessageServer $JAVA_OPTS -Dlogging.config=file:$MESSAGE_SERVER_HOME/config/logback.xml -Dspring.config.location=$MESSAGE_SERVER_HOME/config/application.yml -jar $MESSAGE_SERVER_HOME/lib/damoa-shinhan-va-agent.jar"
    $RUN_JAVA -Djava.security.egd=file:/dev/urandom -DDamoaMessageServer $JAVA_OPTS -Dlogging.config=file:$MESSAGE_SERVER_HOME/config/logback.xml -Dspring.config.location=$MESSAGE_SERVER_HOME/config/application.yml -jar $MESSAGE_SERVER_HOME/lib/damoa-shinhan-va-agent.jar
else
    echo "Another scheduler instance is already started in this folder."
    exit 0
fi
}



shutdown() {
PRG="$0"
PRGDIR=`dirname "$PRG"`
MESSAGE_SERVER_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

PID_FILE="${MESSAGE_SERVER_HOME}"/bin/message-server.pid

if [ ! -f "${PID_FILE}" ]; then
    echo "No schduler instance is running."
    exit 0
fi

PID=$(cat "${PID_FILE}");
if [ -z "${PID}" ]; then
    echo "No schduler instance is running."
    exit 0
else
   if [ $(ps ax | grep $PID | grep -v grep | wc -l) == 0 ]
   then
      rm "${PID_FILE}"
      echo "프로세스가 없습니다."
   fi
   kill -15 "${PID}"

   for (( ; ; ))
   do
      sleep 5

      echo "ps ax | grep $PID"
      ps ax | grep $PID
      echo "$(ps ax | grep $PID | grep -v grep | wc -l)"
      ps ax | grep $PID | grep -v grep | wc -l

      if [ $(ps ax | grep $PID | grep -v grep | wc -l) == 0 ]
      then
         echo "프로세스가 종료되었습니다."
         break;
      fi

      echo "프로세스가 종료되지 않았습니다. 5초 후 다시 확인합니다"
   done


   rm "${PID_FILE}"
   echo "Scheduler Instance with PID ${PID} shutdown."
   exit 0
fi
}



case $1 in
"start")
   start
;;
"stop")
   shutdown
;;
"restart")
   stop
   sleep 5
   start
;;
"console")
   console
;;
*)
   showHelp
esac
