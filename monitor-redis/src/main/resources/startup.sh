#!/bin/bash
application="monitor-task"
logPath="/home/log/monitor"
deployPath="/home/deploy/monitor-redis/"

echo ================$logPATH===============
if [ ! -d $logPATH ];then
 sudo mkdir -p $logPath;
fi

# -Xdebug -Xrunjdwp:transport=dt_socket,server=n,address=8000

cd $deployPath;
setsid java -cp .:conf/*:lib/* -Xmx128m -Xms128m -verbose:gc -Xloggc:$logPath/task-gc.log -XX:CMSInitiatingOccupancyFraction=80 -XX:+UseCMSCompactAtFullCollection -XX:MaxTenuringThreshold=10 -XX:MaxPermSize=64M -XX:SurvivorRatio=3 -XX:NewRatio=2 -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+UseParNewGC -XX:+UseConcMarkSweepGC com.sst.bootstrap.Bootstrap &
