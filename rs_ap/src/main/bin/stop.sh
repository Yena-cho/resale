#!/usr/bin/env bash

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
   kill -15 "${PID}"

   for (( ; ; ))
   do
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

      sleep 5
   done


   rm "${PID_FILE}"
   echo "Scheduler Instance with PID ${PID} shutdown."
   exit 0
fi