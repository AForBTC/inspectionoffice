#!/bin/sh
PID=`jps | grep kbm-ws | awk '{print $1}'`

MAX_COUNT=10
COUNT=0

# 退出正在运行的程序
while [ "$PID" ]
do
  if [ $COUNT = $MAX_COUNT ]; then
    echo "Can not Stop Running kbm-ws, please kill it manually"
    exit 1
  fi
  kill $PID
  echo "Waiting Running kbm-ws stopped"
  sleep 3
  PID=`jps | grep kbm-ws | awk '{print $1}'`
  let COUNT++
done

echo "kbm-ws stopped"