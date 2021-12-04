#!/bin/bash

echo "> Build 파일 복사"

cp /home/ec2-user/app/build/libs/monthSub-0.0.1-SNAPSHOT.jar /home/ec2-user/app/

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

JAR_NAME=$(ls -tr /home/ec2-user/app/monthSub-0.0.1-SNAPSHOT.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

sudo chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

cd /home/ec2-user/app

source /home/ec2-user/app/env

source /home/ec2-user/.bashrc

nohup java -jar $JAR_NAME -Dspring.config.location=classpath:/application.yml &

chmod 755 nohup.out
