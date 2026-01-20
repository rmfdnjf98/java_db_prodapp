📦 Socket 기반 상품 관리 프로그램 (Java + MySQL)

Java Socket 통신과 JSON(Gson) 을 이용해
클라이언트–서버 구조로 상품 CRUD를 구현한 프로젝트입니다.

📌 주요 기능
기능	명령어	설명
상품 목록 조회	get	전체 상품 조회
상품 상세 조회	get {id}	특정 상품 조회
상품 등록	post {name} {price} {qty}	상품 추가
상품 삭제	delete {id}	상품 삭제
종료	exit	클라이언트 종료
🧱 프로젝트 구조
src
├── client
│   └── MyClient.java
│
├── server
│   ├── MyServer.java
│   ├── DBConnection.java
│   ├── Product.java
│   ├── ProductRepository.java
│   ├── ProductService.java
│   └── ProductServiceInterface.java
│
└── dto
├── RequestDTO.java
└── ResponseDTO.java

🧠 아키텍처 개요
[Client]
↓ JSON(RequestDTO)
[Server]
↓
[Service]
↓
[Repository]
↓
[MySQL]


Client : 사용자 입력 → JSON 요청 전송

Server : 요청 파싱 → 서비스 호출 → 응답 생성

Service : 비즈니스 로직 처리

Repository : DB 접근 (JDBC)

DTO : 데이터 전달 전용 객체

📄 DTO 구조
RequestDTO
{
"method": "get | post | delete",
"querystring": {
"id": 1
},
"body": {
"name": "사과",
"price": 1000,
"qty": 10
}
}

ResponseDTO
{
"msg": "ok | error message",
"body": {...}
}

🧑‍💻 실행 방법
1️⃣ MySQL 테이블 생성
CREATE DATABASE productdb;
USE productdb;

CREATE TABLE product_tb (
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100),
price INT,
qty INT
);

2️⃣ 서버 실행
MyServer 실행


포트: 20000

클라이언트 접속 대기 상태

3️⃣ 클라이언트 실행
MyClient 실행

🧪 사용 예시
📌 상품 등록
post 딸기 3000 5

📌 상품 목록 조회
get

📌 상품 상세 조회
get 2

📌 상품 삭제
delete 2

🔍 핵심 코드 설명
🔹 JSON 숫자 파싱 이유
Integer price = ((Number) body.get("price")).intValue();


Gson은 숫자를 Double로 파싱

Object → Number → int 변환 필요

🔹 상품 상세 조회 흐름
Client(get 2)
→ RequestDTO
→ Server
→ ProductService.상품상세(id)
→ ProductRepository.findById(id)
→ MySQL
→ Product
→ ResponseDTO

⚠️ 주의 사항

ResultSet rs.next()는 한 번만 호출

querystring과 body는 동시에 사용하지 않음

exit 입력 시 클라이언트 종료

🛠 기술 스택

Java 17+

Socket API

Gson

JDBC

MySQL

Lombok

📚 학습 포인트

이 프로젝트를 통해 다음 개념을 학습할 수 있습니다:

Socket 기반 통신

JSON 직렬화 / 역직렬화

DTO 설계

Layered Architecture

JDBC CRUD

예외 흐름 제어

Object vs Wrapper 클래스

✨ 정리

이 프로젝트는 Spring 없이
서버 구조의 핵심 개념을 직접 구현하며 이해하는 것을 목표로 합니다.