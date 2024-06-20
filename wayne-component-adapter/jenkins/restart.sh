#!/bin/sh

## 判断是否输入了两个参数
if [ $# -lt 2 ]; then
    usage
fi


## 脚本名称
APP_NAME=$1;

## 操作
OPERATION=$2;

## 运行模式
MODEL=$3

if [ -z "$MODEL" ]; then
    MODEL="dev"
fi

## 其它参数处理
OTHER_ARGS=""
if [ $# -gt 3 ]; then
    shift 3
else
    shift $#
fi
for ARG in $*
do
  OTHER_ARGS="$OTHER_ARGS $ARG"
done


## 脚本所在目录，绝对路径表示
BASE_PATH=$(cd `dirname $APP_NAME`;pwd)

## 去掉所有目录后的脚本名
APP_NAME=${APP_NAME##*/}

### 项目名称
SERVICE_NAME=${APP_NAME%.*}

## 脚本的路径，绝对路径表示
APP_PATH=$BASE_PATH"/"$APP_NAME

JVM_OPT=" ${JVM_OPT} -Dfile.encoding=UTF-8 "
JVM_OPT=" ${JVM_OPT} -Duser.timezone=GMT+0 "
JVM_OPT=" ${JVM_OPT} -Xms512m "
JVM_OPT=" ${JVM_OPT} -Xmx512m "
JVM_OPT=" ${JVM_OPT} -XX:+UseG1GC "
JVM_OPT=" ${JVM_OPT} -XX:+DisableExplicitGC "
JVM_OPT=" ${JVM_OPT} -XX:MetaspaceSize=256M "
JVM_OPT=" ${JVM_OPT} -XX:MaxMetaspaceSize=512M "
JVM_OPT=" ${JVM_OPT} -XX:+HeapDumpOnOutOfMemoryError "
JVM_OPT=" ${JVM_OPT} -XX:HeapDumpPath=${BASE_PATH}/error.hprof "


## 启动程序
start(){
  #is_running
  if [ $? -eq "1" ]; then
    echo "${APP_NAME} is already running. pid is ${PID} ."
  else
    ## 启动 jar 包
    echo "${APP_NAME} starting ...... "
    /usr/bin/java ${JVM_OPT} ${JAVA_OPT} -jar ${OTHER_ARGS} -Dspring.profiles.active=${MODEL} ${APP_PATH}
    #nohup ${JAVA_CMD} -jar ${OTHER_ARGS} -Dspring.profiles.active=${MODEL} ${APP_PATH} > catalina.out 2>&1 &
    echo "${APP_NAME} started  completed "
  fi
}


case "$OPERATION" in
  "start")
    start ;;
  *)
    usage ;;
esac
exit 0