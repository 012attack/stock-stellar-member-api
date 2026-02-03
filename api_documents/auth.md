# Auth API

**Base URL**: `http://localhost:8081/member-api/api/auth`

---

## API 목록

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/register` | 회원가입 | 불필요 |
| POST | `/login` | 로그인 | 불필요 |
| POST | `/refresh` | 토큰 갱신 | Cookie |
| POST | `/logout` | 로그아웃 | Optional |

---

## POST /register

> 회원가입

### Request Body

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| username | string | O | 사용자명 |
| password | string | O | 비밀번호 |
| name | string | O | 이름 |

### Response `201 Created`

| Field | Type | Description |
|-------|------|-------------|
| id | long | 회원 ID |
| username | string | 사용자명 |
| name | string | 이름 |

### Response Headers

| Header | Value |
|--------|-------|
| Location | `/api/members/{id}` |

---

## POST /login

> 로그인

### Request Body

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| username | string | O | - | 사용자명 |
| password | string | O | - | 비밀번호 |
| rememberMe | boolean | X | false | 로그인 유지 |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| message | string | "로그인 성공" |
| expiresIn | long | 토큰 만료 시간 (초) |

### Response Cookies

| Cookie | HttpOnly | Description |
|--------|----------|-------------|
| refreshToken | O | 토큰 갱신용 |

---

## POST /refresh

> 토큰 갱신

### Request Cookies

| Cookie | Required | Description |
|--------|----------|-------------|
| refreshToken | O | 리프레시 토큰 |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| message | string | "토큰 갱신 성공" |
| expiresIn | long | 토큰 만료 시간 (초) |

---

## POST /logout

> 로그아웃

### Request Headers

| Header | Required | Description |
|--------|----------|-------------|
| Authorization | X | Bearer {accessToken} |

### Request Cookies

| Cookie | Required | Description |
|--------|----------|-------------|
| refreshToken | X | 리프레시 토큰 |

### Response `204 No Content`

응답 본문 없음
