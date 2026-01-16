#!/usr/bin/env bash

export APP_HOME=/home/damoa/app/server/apiagent

PID_FILE="${APP_HOME}"/bin/api-agent.pid

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
