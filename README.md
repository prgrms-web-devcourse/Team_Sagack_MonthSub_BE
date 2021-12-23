# Monthsub
![image](https://user-images.githubusercontent.com/70589857/147059348-66c7769a-d6ce-4e0c-8de6-2fed65a274c4.png)

[Monthsub 바로가기](https://monthsub.netlify.app)

## 🤗 프로젝트 소개
- 프로젝트 기간 : 2021/11/26 ~ 
- 작가와 독자의 연결고리, 모두의 출판 플랫폼 MonthSub!
---
## 🧚‍♀️ 팀 소개

|                          **김다희 - 팀장, PO**                          |                          **김은서 - 팀원**                          |                          **유지훈 - 팀원**                          |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
|                 **<img width="1200" alt="다희" src="https://user-images.githubusercontent.com/81504103/147106038-631f0898-2afd-4f12-ab27-9560fb99be1e.png">**                 |                   **<img width="1210" alt="은서" src="https://user-images.githubusercontent.com/81504103/147106990-9630996b-17e4-4557-b491-342f3021af43.png">**                   |                   **<img width="1300" alt="" src="https://user-images.githubusercontent.com/81504103/147187537-57f65db8-203d-47ed-9644-01be307a6c0a.png">**                   |
|                          담당 기능                           |                          담당 기능                           |                          담당 기능                           |
| ･*프로젝트 아키텍처 구성* <br/>･*API 개발*<br/> ･*CI/CD 구축*<br/>･*Cloud Watch 구축*<br/>･*HTTPS 연결*<br/> ･*S3 이미지 업로드 및 삭제 구현* <br/>･  *S3 File 리소스 관리 시스템 설계  및 구현* <br> ･*Criteria를 이용한 커서기반 페이지네이션*<br/>･*Git hooks 지라 티켓 번호 자동화* <br/>･*프로젝트 기획*| ･*JWT(Json Web Token) 발급 구현* <br/>･*API 개발*<br/> ･*토큰 유무에 따른 권한 체크 구현*<br/>･*CORS 처리 구현*<br/>･*유저 로그 관리 구현*<br/> ･*Flyway DDL 형상관리* <br/>･  *DB Seed 관리* <br> ･*AWS RDS 이용하여 로컬, 운영 DB 관리*<br/>･*결제 시스템 설계 및 개발* <br/>･*결제 동시 요청 낙관적 락으로 관리* |･*JDK17 sealed와 record를 활용한 DTO 구현* <br/>･*Scheduler를 활용한 DB 상태값 관리 *<br/> ･*전체적인 DB 설계*<br/>･*핵심 비지니스 설계 및 구현*<br/>･*시리즈 좋아요 시스템 설계 및 기능 구현*<br/> ･*회원 팔로우 시스템 설계 및 기능 구현* <br/>･  *프로젝트 예외처리 기능구현* <br>

<br/>

---
## 🚀 기술스택
### 개발 환경
<p align="left">
  <img src="https://img.shields.io/badge/Java17-007396?style=flat-square&logo=Java&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/Spring Boot 2.5.6-6DB33F?style=flat-square&logo=Spring&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=SpringSecurity&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/-JPA-gray?logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/-Criteria-blue?logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/MySQL 8-4479A1?style=flat-square&logo=MySQL&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/Gradle-4429A1?style=flat-square&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/S3-232F3E?style=flat-square&logo=amazon%20AWS&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/CodeDeploy-232F3E?style=flat-square&logo=amazon%20AWS&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/ELB-232F3E?style=flat-square&logo=amazon%20AWS&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/Flyway-23223E?style=flat-square&logo=Flyway%20AWS&logoColor=white&style=flat"/></a>

### 협업 툴
  <img src="https://img.shields.io/badge/Jira-0052CC?style=flat-square&logo=Jira%20software&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=Swagger&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/Notion-VC52CA?style=flat-square&logo=Notion%20software&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/slack-232F3E?style=flat-square&logo=slack&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/Git Hook-007396?style=flat-square&logoColor=white&style=flat"/></a>

### 기타
  <img src="https://img.shields.io/badge/ERDCloud-4429A7?style=flat-square&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/Postman-0052CC?style=flat-square&logo=Postman&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/Husky-232F3E?style=flat-square&logoColor=white&style=flat"/></a>
</p>

---
## 🏗️ 설계

### CICD
<img width="1200" alt="cicd" src="https://user-images.githubusercontent.com/81504103/146681744-26746d10-558d-41f0-ba86-1c8814af6130.png">

### Infra Architecture​
<img width="1200" alt="architecture" src="https://user-images.githubusercontent.com/81504103/146681427-21e24399-ac82-4434-a3f4-a90e04ab2896.png">

### Monthsub Architecture
<img width="1200" alt="architecture" src="https://user-images.githubusercontent.com/81504103/147188674-9ba012a4-88e5-4a0f-afaf-41f32b220872.png">

### ERD
<img width="1200" alt="erd" src="https://user-images.githubusercontent.com/81504103/146680870-366a3912-9380-4489-8293-5d537e52526b.png">

### API 문서
[Swagger API 문서](https://monthsub.com/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#)

---
## 💻 개발 환경 설정 
- 프로젝트 설치

```bash
$ git clone https://github.com/prgrms-web-devcourse/Team_Sagack_MonthSub_BE

# 프로젝트 루트 디렉토리에서 실행 
# husky 설치로 git hook 관리 
$ npm install
```

- 필요 환경 변수

```bash
DB_URL=userHost
DB_USER=userName
DB_PASSWORD=userPassword
```

- 환경 변수가 설정 되어 있지 않다면 Intellij Plugin 설치
- [Intellij Plugin - EnvFile 설치](https://plugins.jetbrains.com/plugin/7861-envfile)

> Project root 경로에 `.env` 파일 생성 후 `필요 환경 변수` 작성

- EnvFile 플러그인 설정

<details>
<summary>설정 캡쳐 (펼치기)</summary>
<div markdown="1">
	<img src="https://user-images.githubusercontent.com/20269425/140634606-e092aad9-f273-49a0-a976-f432d74f3a36.png" alt="Run -> Edit configuration"  height="600px">
  <img src="https://user-images.githubusercontent.com/20269425/140716435-a7a96611-cf37-4483-ada2-313291af2371.png" alt="Spring boot EnvFile"  height="600px">
</div>
</details>


## 🔨 기타 규칙
- 깃 커밋 규칙
  - 브랜치명은 `feat/티켓이름`으로 한다
  - git hook에 의해 해당 브랜치에서 커밋시 타이틀에 자동으로 티켓이름이 들어간다 
