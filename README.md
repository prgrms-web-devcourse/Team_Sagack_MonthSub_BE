# Monthsub
![image](https://user-images.githubusercontent.com/70589857/147059348-66c7769a-d6ce-4e0c-8de6-2fed65a274c4.png)

[Monthsub 바로가기](https://monthsub.netlify.app)

## 🤗 프로젝트 소개
- 프로젝트 기간 : 2021/11/26 ~ 
- 작가와 독자의 연결고리, 모두의 출판 플랫폼 MonthSub!
---
## 🧚‍♀️ 팀 소개

|김다희|김은서|유지훈|
|:---:|:---:|:---:|
|<img src="https://user-images.githubusercontent.com/81504103/147106038-631f0898-2afd-4f12-ab27-9560fb99be1e.png" width="60%" />|<img src="https://user-images.githubusercontent.com/81504103/147106990-9630996b-17e4-4557-b491-342f3021af43.png" width="60%" />|<img src="https://user-images.githubusercontent.com/70589857/147061670-e47bfdc2-ba54-4083-a403-cd3068ad6ae0.png" width="60%" />


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
