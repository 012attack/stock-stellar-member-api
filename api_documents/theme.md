# Theme API

**Base URL**: `http://localhost:8081/member-api/api/themes`

---

## API 목록

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/` | 테마 목록 조회 | 필요 |

---

## GET /

> 테마 목록 조회 (페이징, 검색 필터 지원)

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | int | X | 0 | 페이지 번호 (0부터 시작) |
| size | int | X | 20 | 페이지 당 항목 수 |
| themeName | string | X | - | 테마명으로 검색 (부분 일치) |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| themes | array | 테마 목록 |
| page | int | 현재 페이지 |
| size | int | 페이지 크기 |
| totalElements | long | 전체 항목 수 |
| totalPages | int | 전체 페이지 수 |

### Theme Object

| Field | Type | Description |
|-------|------|-------------|
| id | int | 테마 ID |
| themeName | string | 테마명 |

### Example Request

```
GET /member-api/api/themes?page=0&size=10
GET /member-api/api/themes?page=0&size=10&themeName=반도체
```

### Example Response

```json
{
  "themes": [
    {
      "id": 1,
      "themeName": "반도체"
    },
    {
      "id": 2,
      "themeName": "AI"
    },
    {
      "id": 3,
      "themeName": "2차전지"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 50,
  "totalPages": 5
}
```
