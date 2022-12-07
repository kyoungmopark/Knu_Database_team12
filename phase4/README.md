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
