#!/bin/sh

PID=`jps | grep inspectionoffice-ws | awk '{print $1}'`

MAX_COUNT=10
COUNT=0

# 先退出正在运行的程序
while [ "$PID" ]
do
  if [ $COUNT = $MAX_COUNT ]; then
    echo "Can not Stop Running inspectionoffice-ws, please kill them first manually"
    exit 1
  fi
  kill $PID
  echo "Waiting Running inspectionoffice-ws stopped"
  sleep 3
  PID=`jps | grep inspectionoffice-ws | awk '{print $1}'`
  let COUNT++
done

echo "Start to run inspectionoffice-ws"

BASE_PATH=$(cd `dirname $0`;pwd)

JAVA=$JAVA_HOME/bin/java
echo $JAVA

ConfigLocation=file://${BASE_PATH}/config/application.properties

# 指定日志配置文件
LOG_FILE=${BASE_PATH}/config/logback-spring.xml
HEAP_OPTS="-Xmx4G -Xms1G"

#日志文件输出目录
LOG_PATH="${BASE_PATH}/logs"

echo ${BASE_PATH}

nohup java -jar $HEAP_OPTS  -Dproject.home=${BASE_PATH} -Dinspectionoffice-ws.log=${LOG_PATH} -Dlogging.config=${LOG_FILE} -Dspring.config.location=${ConfigLocation} -XX:HeapDumpPath=$BASE_PATH/inspectionoffice-ws/integration.hprof $BASE_PATH/inspectionoffice-ws.jar  -Djava.ext.dirs=$JAVA_HOME/lib >$BASE_PATH/inspectionoffice-ws.log >/dev/null 2>&1 &