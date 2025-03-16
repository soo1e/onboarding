# **IntelliPick Onboarding API** 🏗️

LINK : 3.39.10.79:8080/swagger-ui/index.html

> **Spring Boot 기반의 회원가입, 로그인, JWT 인증, 관리자 권한 관리 API**  
> 이 프로젝트는 **Spring Security + JWT** 기반의 인증 시스템을 구축하며,  
> 회원가입, 로그인, JWT 인증 및 관리자 권한 부여 기능을 제공합니다.  

## **🛠️ 기술 스택 (Tech Stack)**  
- **Backend:** Java 17, Spring Boot 3, Spring Security, JPA (Hibernate), PostgreSQL  
- **Build Tool:** Gradle  
- **Auth:** JWT (Json Web Token)  
- **Docs:** Swagger UI (SpringDoc)  
- **Testing:** JUnit 5, Mockito  

---

## **📌 실행 방법 (Getting Started)**  
### **1. 환경 변수 설정**


### **2. 프로젝트 빌드 및 실행**
```bash
# 1. Gradle 빌드
./gradlew build

# 2. JAR 파일 실행
java -jar build/libs/onboarding-0.0.1-SNAPSHOT.jar
```

### **3. API 문서 확인 (Swagger UI)**
API 테스트 및 문서를 보려면 [Swagger UI](3.39.10.79:8080/swagger-ui/index.html) 접속  

---

## **📌 주요 기능 (Features)**  
### ✅ **1. 회원가입 (Signup)**
- 신규 사용자를 등록합니다.  
- 중복된 사용자명(username)이나 닉네임(nickname)이 존재하면 오류 발생  

```http
POST /api/signup
```
#### 📌 **요청 예시 (Request)**
```json
{
  "username": "testuser",
  "password": "testpassword",
  "nickname": "tester"
}
```
#### ✅ **응답 예시 (Response)**
```json
{
  "username": "testuser",
  "nickname": "tester",
  "roles": [
    { "role": "USER" }
  ]
}
```
#### ❌ **이미 가입된 사용자일 경우**
```json
{
  "error": {
    "code": "USER_ALREADY_EXISTS",
    "message": "이미 가입된 사용자입니다."
  }
}
```

---

### ✅ **2. 로그인 (Login)**
- 회원 로그인을 수행하며, 성공 시 JWT 토큰을 발급합니다.  
- ID/PW가 일치하지 않으면 `INVALID_CREDENTIALS` 오류 반환  

```http
POST /api/login
```
#### 📌 **요청 예시 (Request)**
```json
{
  "username": "testuser",
  "password": "testpassword"
}
```
#### ✅ **응답 예시 (Response)**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5..."
}
```
#### ❌ **로그인 실패 시**
```json
{
  "error": {
    "code": "INVALID_CREDENTIALS",
    "message": "아이디 또는 비밀번호가 올바르지 않습니다."
  }
}
```

---

### ✅ **3. 관리자 권한 부여 (Grant Admin Role)**
- `ROLE_ADMIN` 권한을 가진 사용자만 다른 사용자에게 관리자 권한을 부여할 수 있습니다.  

```http
POST /admin/users/{userId}/roles
```
#### 📌 **요청 헤더 (Headers)**
```json
Authorization: Bearer <JWT_TOKEN>
```
#### ✅ **응답 예시 (Response)**
```json
{
  "username": "testuser",
  "nickname": "tester",
  "roles": [
    { "role": "Admin" }
  ]
}
```
#### ❌ **권한이 부족한 경우**
```json
{
  "error": {
    "code": "ACCESS_DENIED",
    "message": "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."
  }
}
```

---

## **📌 JWT 인증 (Authentication with JWT)**
모든 보호된 API 요청에는 **HTTP 헤더에 JWT 토큰**을 포함해야 합니다.  
헤더 형식:  
```http
Authorization: Bearer <토큰값>
```
서버는 다음을 검증합니다.  
✅ **토큰 존재 여부 확인**  
✅ **서명 검증 (서버의 비밀 키로 서명 확인)**  
✅ **토큰 만료 여부 확인**  
✅ **토큰에 포함된 권한 정보 추출**  
✅ **SecurityContext에 인증 정보 설정**  
✅ **요청한 API에 접근 권한이 있는지 확인**  

### ❌ **토큰이 유효하지 않거나 만료된 경우**
```json
{
  "error": {
    "code": "INVALID_TOKEN",
    "message": "유효하지 않은 인증 토큰입니다."
  }
}
```
### ❌ **권한이 부족한 경우**
```json
{
  "error": {
    "code": "ACCESS_DENIED",
    "message": "접근 권한이 없습니다."
  }
}
```

---

## **📌 테스트 실행 (Run Tests)**
```bash
./gradlew test
```
테스트 항목:  
✅ 회원가입 (`UserServiceTest`)  
✅ 로그인 (`AuthServiceTest`)  
✅ 관리자 권한 부여 (`AdminControllerTest`)  
✅ JWT 인증 (`JwtUtilTest`)  

---

## **📌 프로젝트 구조 (Directory Structure)**  
```
📂 src
 ├── main/java/com/intellipick/onboarding
 │   ├── auth
 │   │   ├── controller  # API 컨트롤러
 │   │   ├── dto         # 요청/응답 DTO
 │   │   ├── entity      # JPA 엔티티
 │   │   ├── exception   # 커스텀 예외
 │   │   ├── repository  # JPA 레포지토리
 │   │   ├── security    # JWT & Security 설정
 │   │   ├── service     # 비즈니스 로직
 │   ├── config          # 전역 설정 (Security, Swagger 등)
 │   ├── exception       # 공통 예외 처리
 │   ├── utils           # 유틸리티 클래스
 ├── test/java/com/intellipick/onboarding
 │   ├── auth/service    # 서비스 레이어 테스트
 │   ├── auth/security   # JWT 관련 테스트
 ├── resources
 │   ├── application.yml # 환경 설정
 │   ├── schema.sql      # 초기 테이블 스키마
 ├── build.gradle        # Gradle 빌드 설정
 ├── README.md           # 프로젝트 설명
```

---
