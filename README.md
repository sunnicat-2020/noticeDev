
## ๐ ๊ณต์ง์ฌํญ ๊ด๋ฆฌ REST API ๊ตฌํ

### ๐จ ์๊ฐ 

-----

๊ณต์ง์ฌํญ์ ๋ฑ๋ก, ์์ , ์ญ์ , ์กฐํํ๋ REST API ๋ฅผ ๊ตฌํํ๋ค.



### ๐จ ๊ฐ๋ฐ ํ๊ฒฝ

-----

- Java : Zulu JDK 11.56.19
- DBMS : Maria DB 10.6
- Spring Boot, JPA, JUnit5 Test

### ๐จ  DB ์คํฌ๋ฆฝํธ

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

### ๐จ  ๊ตฌํ ๋ชฉ๋ก

-----

1. ๊ณต์ง์ฌํญ ๋ฑ๋ก
   1. ์๋ ฅํญ๋ชฉ: ์ ๋ชฉ, ๋ด์ฉ, ๊ณต์ง ์์์ผ์, ๊ณต์ง ์ข๋ฃ์ผ์, ์ฒจ๋ถํ์ผ(์ฌ๋ฌ๊ฐ)
   2. ์๋ตํญ๋ชฉ: ์ฑ๊ณต์ฌ๋ถ, ๋ฉ์์ง, ๋ฑ๋ก๋ ๊ณต์ง์ฌํญ ์์ด๋
```
curl --location --request POST 'http://127.0.0.1:8080/notice' \
--form 'notice="{\"title\": \"ํ์ดํ\", \"content\": \"๋ด์ฉ\", \"writer\": \"์์ฑ์\", \"start_dt\": \"2022-05-15 00: 00:00\", \"end_dt\": \"2022-06-15 15:00:00\"}"' \
--form 'file=@"/path/to/file"''
```
2. ๊ณต์ง์ฌํญ ์์ 
   1. ์๋ ฅํญ๋ชฉ: ์์ ์ฌํญ ์ ๋ชฉ, ๋ด์ฉ, ๊ณต์ง ์์์ผ์, ๊ณต์ง ์ข๋ฃ์ผ์, ์ฒจ๋ถํ์ผ(์ฌ๋ฌ๊ฐ)
   2. ์๋ตํญ๋ชฉ: ์ฑ๊ณต์ฌ๋ถ, ๋ฉ์์ง, ์์ ๋ ๊ณต์ง์ฌํญ ์์ด๋
```
curl --location --request PUT 'http://127.0.0.1:8080/notice' \
--form 'notice="{\"id\": \"35\", \"title\": \"๋ณ๊ฒฝ\"}"' \
--form 'file=@"/path/to/file"'
```

3. ๊ณต์ง์ฌํญ ์ญ์ 
   1. ์๋ ฅํญ๋ชฉ: ๊ณต์ง์ฌํญ ์์ด๋
   2. ์๋ตํญ๋ชฉ: ์ฑ๊ณต์ฌ๋ถ, ๋ฉ์์ง
```
curl --location --request DELETE 'http://127.0.0.1:8080/notice/{id}'
```

4. ๊ณต์ง์ฌํญ ์กฐํ
   1. ์๋ ฅํญ๋ชฉ: ๊ณต์ง์ฌํญ ์์ด๋
   2. ์๋ตํญ๋ชฉ: ์ฑ๊ณต์ฌ๋ถ, ๋ฉ์์ง, ๊ณต์ง์ฌํญ(์ ๋ชฉ, ๋ด์ฉ, ๋ฑ๋ก์ผ์, ์กฐํ์, ์์ฑ์)
```
curl --location --request GET 'http://127.0.0.1:8080/notice'
```

### ๐จ  ๊ธฐ๋ ๋ฐฉ๋ฒ

-----

```
java -jar notice-0.0.1-SNAPSHOT.jar
```