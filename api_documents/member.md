# Member API

**Base URL**: `http://localhost:8081/member-api/api/members`

---

## API 목록

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/` | 회원 목록 조회 | 필요 |

---

## GET /

> 회원 목록 조회

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | int | X | 0 | 페이지 번호 (0부터 시작) |
| size | int | X | 20 | 페이지 당 항목 수 |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| members | array | 회원 목록 |
| page | int | 현재 페이지 |
| size | int | 페이지 크기 |
| totalElements | long | 전체 항목 수 |
| totalPages | int | 전체 페이지 수 |

### Member Object

| Field | Type | Description |
|-------|------|-------------|
| id | long | 회원 ID |
| username | string | 사용자명 |
| name | string | 이름 |

### Example Response

```json
{
  "members": [
    { "id": 1, "username": "user1", "name": "홍길동" },
    { "id": 2, "username": "user2", "name": "김철수" }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```
