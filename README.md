#공지사항 관리 REST API 구현

-----

## 개발 환경

-----

- 개발 언어 : Zulu JDK 11.56.19
- DB : Maria DB 10.6

## DB 스크립트

-----

```
CREATE TABLE NOTICE (
	NOTICE_ID INT AUTO_INCREMENT PRIMARY KEY,
	TITLE VARCHAR(100),
	CONTENT VARCHAR(1000),
	WRITER VARCHAR(100),
	START_DT DATETIME,
	END_DT DATETIME,
	CREATE_DT DATETIME DEFAULT NOW(),
	VIEW_COUNT INT DEFAULT 0
);
CREATE TABLE ATTACHMENT (
    ATTACH_ID INT AUTO_INCREMENT PRIMARY KEY,
    NOTICE_ID INT,
    FILE_NAME VARCHAR(200),
    FILE_CONTENT BLOB
);
```

## 구현 목록

-----

1. 공지사항 등록
   1. 입력항목: 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일(여러개)
   2. 응답항목: 성공여부, 메시지, 등록된 공지사항 아이디
```
curl --location --request POST 'http://127.0.0.1:8080/notice' \
--form 'notice="{\"title\": \"타이틀\", \"content\": \"내용\", \"writer\": \"작성자\", \"start_dt\": \"2022-05-15 00: 00:00\", \"end_dt\": \"2022-06-15 15:00:00\"}"' \
--form 'file=@"/path/to/file"''
```
2. 공지사항 수정
   1. 입력항목: 수정사항 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일(여러개)
   2. 응답항목: 성공여부, 메시지, 수정된 공지사항 아이디
```
curl --location --request PUT 'http://127.0.0.1:8080/notice' \
--form 'notice="{\"id\": \"35\", \"title\": \"변경\"}"' \
--form 'file=@"/path/to/file"'
```

3. 공지사항 삭제
   1. 입력항목: 공지사항 아이디
   2. 응답항목: 성공여부, 메시지
```
curl --location --request DELETE 'http://127.0.0.1:8080/notice/{id}'
```

4. 공지사항 조회
   1. 입력항목: 공지사항 아이디
   2. 응답항목: 성공여부, 메시지, 공지사항(제목, 내용, 등록일시, 조회수, 작성자)
```
curl --location --request GET 'http://127.0.0.1:8080/notice'
```