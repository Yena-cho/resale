#!/usr/bin/env bash


PRG="$0"
PRGDIR=`dirname "$PRG"`
API_SERVER_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

PID_FILE="${API_SERVER_HOME}"/bin/api-server.pid

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
   rm "${PID_FILE}"
   echo "Scheduler Instance with PID ${PID} shutdown."
   exit 0
fi
