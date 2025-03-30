### 동시성 프로 젝트 

기술 스택 : java 17 , mysql , docker , intelliJ IDE


**작업환경 세팅**

```text
작업환경 세팅

docker 설치
brew install docker 
brew link docker
docker version


mysql 설치 및 실행
docker pull mysql
docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1234 --name mysql mysql 
docker ps

docker: no matching manifest for linux/arm64/v8 in the manifest list entries. 오류가 발생하시는분은
docker pull --platform linux/x86_64 mysql

my sql 데이터베이스 생성
docker exec -it mysql bash
mysql -u root -p
create database stock_example;
use stock_example;

```

**프로젝트 생성**

```text
1. yml 설정 후에 application server on

application.yml

spring:
  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/stock_example
    username: root
    password: 1234

logging:
  level:
    org:
      hibernate:
        SQL: DEBUG
        type:
          descriptor:
            sql:
              BasicBinder: TRACE


```