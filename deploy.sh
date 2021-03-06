#!/bin/bash

HOME=/home/ec2-user
LOGS_PATH=$HOME/app/logs

mkdir -p $LOGS_PATH

echo "> 이전 로그 삭제"
find $LOGS_PATH -type f -name "*.log"
find $LOGS_PATH -type f -name "*.log" -exec rm {} \;

echo "> Build 파일 복사"
cp $HOME/app/build/libs/monthSub-0.0.1-SNAPSHOT.jar $HOME/app/

CURRENT_PID=$(pgrep -fl monthSub | awk '{print $1}')

echo "현재 구동 중인 애플리케이션 pid 확인"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $HOME/app/monthSub-0.0.1-SNAPSHOT.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

sudo chmod +x $JAR_NAME

echo "> 환경 변수 적용"
source $HOME/app/env

source $HOME/.bashrc

echo "> $JAR_NAME 실행"

cd $HOME/app

NOW=$(date +%FT%H:%M:%S)

echo $NOW

nohup java -jar $JAR_NAME -Duser timezone-Asia/Seoul -Dspring.config.location=classpath:/application.yml -Dspring.output.ansi.enabled=NAVER > logs/$NOW.log &
