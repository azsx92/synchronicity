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

### 동시성이란?
- 동시라고 말하면 같은 시간에 함께 실행될 것 같은 느낌이 들지만 번역과정에서 우리말 '동시'에서 느껴지는 느낌때문인지는 모르겠으나 computer science 에서 말하는 동시성(concurrent)은 같은 시간에 함께 실행되는 것을 의미하지 않는다.
  같은 때라고 하면 틀리지만 같은 시기라고 하면 얼추 맞을 수도 있겠다.
  그럼 정확히 Computer Science에서 말하는 동시성(concurrent)란 특정 프로세스의 실행 시간이 다른 프로세스의 흐름과 겹치는 상황에서 동시에 실행한다고 말한다.


### sychronized 란
- 단일 서버 안에서 A 와 B의 동시성 문제가 발생 하게 되면 A를 먼저 처리하고 나서 B를 처리하도록 도와주는 키워드 이다.
- synchronized 단일 서버 안에서는 레이스 컨디션 문제가 발생하지 않지만 
- 최근 에는 서버가 두개 이상 인 경우가 대부분 이므로 레이스 컨디션 문제가 발생 하기가 쉽다. 

### RaseCondition 란
- 스레드 공유 중에 A에 대한 작업이 끝나지 않았지만 B가 접근하여 스레드를 공유하게 되어 문제가 발생되는 것을 rase condition이라고 한다. 
### database 이용하여 해결하기

```text
Mysql 을 활용한 다양한 방법
Pessimistic Lock
실제로 데이터에 Lock 을 걸어서 정합성을 맞추는 방법입니다. exclusive lock 을 걸게되며 다른 트랜잭션에서는 lock 이 해제되기전에 데이터를 가져갈 수 없게됩니다.
데드락이 걸릴 수 있기때문에 주의하여 사용하여야 합니다.


Optimistic Lock
실제로 Lock 을 이용하지 않고 버전을 이용함으로써 정합성을 맞추는 방법입니다. 먼저 데이터를 읽은 후에 update 를 수행할 때 현재 내가 읽은 버전이 맞는지 확인하며 업데이트 합니다. 내가 읽은 버전에서 수정사항이 생겼을 경우에는 application에서 다시 읽은후에 작업을 수행해야 합니다.


Named Lock
이름을 가진 metadata locking 입니다. 이름을 가진 lock 을 획득한 후 해제할때까지 다른 세션은 이 lock 을 획득할 수 없도록 합니다. 주의할점으로는 transaction 이 종료될 때 lock 이 자동으로 해제되지 않습니다. 별도의 명령어로 해제를 수행해주거나 선점시간이 끝나야 해제됩니다.

참고
https://dev.mysql.com/doc/refman/8.0/en/
https://dev.mysql.com/doc/refman/8.0/en/locking-functions.html
https://dev.mysql.com/doc/refman/8.0/en/metadata-locking.html
```

### Redis 라이브러리

#### Lettuce
- setnx  명령어를 활용하여 분산락 구현
- spin lock 방식

#### Redission
- pub-sub 기반으로 Lock 구현 제공

### Redis 작업환경 셋팅
```text
docker pull redis

docker run --name myredis -d -p 6379:6379 redis
```