# Theme API

**Base URL**: `http://localhost:8081/member-api/api/themes`

---

## API 목록

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/` | 테마 목록 조회 | 필요 |
| GET | `/{id}` | 테마 상세 조회 (연결된 뉴스/종목 포함) | 필요 |

---

## GET /

> 테마 목록 조회 (페이징, 검색 필터 지원)

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | int | X | 0 | 페이지 번호 (0부터 시작) |
| size | int | X | 20 | 페이지 당 항목 수 |
| themeName | string | X | - | 테마명으로 검색 (부분 일치) |
| favoriteOnly | boolean | X | false | true일 경우 즐겨찾기한 테마만 조회 (인증 필요) |

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
GET /member-api/api/themes?page=0&size=10&favoriteOnly=true
Authorization: Bearer {token}
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

---

## GET /{id}

> 테마 상세 조회 (연결된 뉴스 및 종목 포함)

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | int | O | 테마 ID |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| id | int | 테마 ID |
| themeName | string | 테마명 |
| news | array | 연결된 뉴스 목록 |
| stocks | array | 연결된 종목 목록 |

### News Object (in ThemeDetail)

| Field | Type | Description |
|-------|------|-------------|
| id | int | 뉴스 ID |
| title | string | 뉴스 제목 |
| link | string | 뉴스 링크 |
| press | object | 언론사 (nullable) |
| themes | array | 연결된 테마 목록 |
| createdAt | string | 생성일시 |

### Stock Object (in ThemeDetail)

| Field | Type | Description |
|-------|------|-------------|
| id | int | 종목 ID |
| stockCode | string | 종목코드 |
| stockName | string | 종목명 |
| companySummary | string | 회사 요약 (nullable) |
| themes | array | 연결된 테마 목록 |

### Response `404 Not Found`

테마를 찾을 수 없는 경우

### Example Request

```
GET /member-api/api/themes/1
```

### Example Response

```json
{
  "id": 1,
  "themeName": "반도체",
  "news": [
    {
      "id": 10,
      "title": "반도체 산업 전망",
      "link": "https://example.com/news/10",
      "press": { "id": 1, "name": "한국경제" },
      "themes": [
        { "id": 1, "themeName": "반도체" }
      ],
      "createdAt": "2026-02-15T10:00:00"
    }
  ],
  "stocks": [
    {
      "id": 1,
      "stockCode": "005930",
      "stockName": "삼성전자",
      "companySummary": "반도체 및 전자제품 제조",
      "themes": [
        { "id": 1, "themeName": "반도체" },
        { "id": 2, "themeName": "AI" }
      ]
    }
  ]
}
```
