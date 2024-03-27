#!/bin/sh

PID=`jps | grep kbm-ws | awk '{print $1}'`

MAX_COUNT=10
COUNT=0

# 先退出正在运行的程序
while [ "$PID" ]
do
  if [ $COUNT = $MAX_COUNT ]; then
    echo "Can not Stop Running kbm-ws, please kill them first manually"
    exit 1
  fi
  kill $PID
  echo "Waiting Running kbm-ws stopped"
  sleep 3
  PID=`jps | grep kbm-ws | awk '{print $1}'`
  let COUNT++
done

echo "Start to run kbm-ws"

BASE_PATH=$(cd `dirname $0`;pwd)

JAVA=$JAVA_HOME/bin/java
echo $JAVA

ConfigLocation=file://${BASE_PATH}/config/application.yml

# 指定日志配置文件
LOG_FILE=${BASE_PATH}/config/logback.xml
HEAP_OPTS="-Xmx4G -Xms1G"

#日志文件输出目录
LOG_PATH="${BASE_PATH}/logs"

echo ${BASE_PATH}
echo $BASE_PATH

nohup java -jar $HEAP_OPTS  -Dproject.home=${BASE_PATH} -Dkbm-ws.log=${LOG_PATH} -Dlogging.config=${LOG_FILE} -Dspring.config.location=${ConfigLocation} -XX:HeapDumpPath=$BASE_PATH/kbm-ws/integration.hprof $BASE_PATH/kbm-ws.jar  -Djava.ext.dirs=$JAVA_HOME/lib >$BASE_PATH/kbm-ws.log