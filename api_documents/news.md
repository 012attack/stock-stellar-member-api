# News API

**Base URL**: `http://localhost:8081/member-api/api/news`

---

## API 목록

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/` | 뉴스 목록 조회 | 필요 |
| GET | `/{id}` | 뉴스 상세 조회 | 필요 |

---

## GET /

> 뉴스 목록 조회 (페이징, 검색 필터 지원)

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | int | X | 0 | 페이지 번호 (0부터 시작) |
| size | int | X | 20 | 페이지 당 항목 수 |
| title | string | X | - | 제목 검색 (부분 일치) |
| pressName | string | X | - | 언론사명으로 필터링 (부분 일치) |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| news | array | 뉴스 목록 |
| page | int | 현재 페이지 |
| size | int | 페이지 크기 |
| totalElements | long | 전체 항목 수 |
| totalPages | int | 전체 페이지 수 |

### News Object

| Field | Type | Description |
|-------|------|-------------|
| id | int | 뉴스 ID |
| title | string | 제목 |
| link | string | 뉴스 링크 URL |
| press | object | 언론사 정보 (nullable) |
| createdAt | datetime | 생성일시 |

### Press Object

| Field | Type | Description |
|-------|------|-------------|
| id | int | 언론사 ID |
| name | string | 언론사 이름 |

### Example Request

```
GET /member-api/api/news?page=0&size=10&title=삼성&pressName=한국경제
```

### Example Response

```json
{
  "news": [
    {
      "id": 1,
      "title": "삼성전자 주가 상승",
      "link": "https://example.com/news/1",
      "press": {
        "id": 1,
        "name": "한국경제"
      },
      "createdAt": "2026-02-05T10:30:00"
    },
    {
      "id": 2,
      "title": "삼성 반도체 호황",
      "link": "https://example.com/news/2",
      "press": {
        "id": 1,
        "name": "한국경제"
      },
      "createdAt": "2026-02-05T09:15:00"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 50,
  "totalPages": 5
}
```

---

## GET /{id}

> 뉴스 상세 조회

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | int | O | 뉴스 ID |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| id | int | 뉴스 ID |
| title | string | 제목 |
| link | string | 뉴스 링크 URL |
| press | object | 언론사 정보 (nullable) |
| createdAt | datetime | 생성일시 |

### Response `404 Not Found`

뉴스를 찾을 수 없는 경우

### Example Request

```
GET /member-api/api/news/1
```

### Example Response

```json
{
  "id": 1,
  "title": "삼성전자 주가 상승",
  "link": "https://example.com/news/1",
  "press": {
    "id": 1,
    "name": "한국경제"
  },
  "createdAt": "2026-02-05T10:30:00"
}
```
