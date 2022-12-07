# Phase4 Implementation

## 실행 방법

어플리케이션과 데이터베이스를 Container화 하여 `run.sh` 스크립트를 사용하여 데이터베이스와 웹어플리케이션을 자동으로 시작할 수 있습니다.

`run.sh` 스크립트는 오라클 컨테이너 레지스트리에서 오라클 DBMS 이미지를
받아오는데, 이를 위해 오라클 웹사이트에서 오라클 DBMS 사용권 동의서에 동의하실 필요가 있습니다.

사용권 동의와 관련된 자세한 내용은 [오라클 웹사이트](https://container-registry.oracle.com/ords/f?p=113:4:13898099368784:::::)를 확인해주세요.

```sh
$ sudo ./run.sh
```

## 아키텍처

 - NodeJS + Express + Jade
 - Oracle DBMS 21c

## 개발환경

 - Ubuntu 20.04
 - Docker
 - Eclipse, VSCode, Vim
 - SQLPlus, SQLDeveloper

## 설치

- express-generator
  > (command) npm install -g express-generator
  >
  > > 프로젝트를 사용자가 손쉽게 생성할 수 있게 도와주는 웹 애플리케이션 프레임워크
- npm
- express
  > - (command) npm install express
  > - node_modules, package-lock.json 파일이 생성된다 (node_modules은 .gitignore에 넣는다)
- oracledb
  > (command) npm install oracledb

## 생성

- express {프로젝트 이름}
  > (command - cmd창에서) express library-manager
  >
  > > API 서버를 만들기 위한 프로젝트

## 실행

> (command) npm start

## 풀더 설명

- bin/www
  > 웹 서버 구축에 관한 파일, 포트번호 등 웹 서버 구축에 관한 설정 정보가 정의
- node_modules
  > node.js의 모듈이 설치된
- public
  > 자바스크립트 파일, 이미지 파일, css 등 정적 파일을 위한 풀더
- routes
  > 라우팅 리소스 별로 모듈을 만들어서 라우팅 조직을 구현, 클라이언트의 요청별로 어떤 로직을 수행할 지 정해놓는 파일
  > ex) java controller
- views
  > request 요청에 대한 로직을 처리한 후 클라이언트에 응답을 보낼 때 html 코드로 변환해서 반환하는 파일을 정의
- app.js
  > express 설정 정보가 담겨져 있는 파일
- package.json
  > 프로젝트 이름, 버전, dependencies, 모듈 등 기술된 파일
