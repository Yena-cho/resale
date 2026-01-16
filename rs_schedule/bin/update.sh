#!/bin/bash

cd "$(dirname "$0")"/..
APP_HOME=`pwd`
DEFAULT_BRANCH=develop

cd $APP_HOME/git
echo "git branch"
git branch
echo `git branch | grep "^* develop$" | wc -l`
echo "$DEFAULT_BRANCH branch detected"
git pull origin
mvn clean package -Dmaven.test.skip=true -U

cp -v $APP_HOME/git/target/damoa-scheduler-*.jar $APP_HOME/archive
cp -v $APP_HOME/git/target/damoa-scheduler-*.jar $APP_HOME/lib/damoa-scheduler.jar
