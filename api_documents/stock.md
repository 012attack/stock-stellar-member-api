# Stock API

**Base URL**: `http://localhost:8081/member-api/api/stocks`

---

## API 목록

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/` | 종목 목록 조회 | 필요 |
| GET | `/{id}` | 종목 상세 조회 (ID) | 필요 |
| GET | `/code/{stockCode}` | 종목 상세 조회 (종목코드) | 필요 |
| POST | `/{stockId}/themes` | 종목에 테마 추가 | 필요 |
| DELETE | `/{stockId}/themes/{themeId}` | 종목에서 테마 제거 | 필요 |

---

## GET /

> 종목 목록 조회 (페이징, 검색 필터 지원)

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | int | X | 0 | 페이지 번호 (0부터 시작) |
| size | int | X | 20 | 페이지 당 항목 수 |
| stockName | string | X | - | 종목명으로 검색 (부분 일치) |
| stockCode | string | X | - | 종목코드로 검색 (부분 일치) |
| themeName | string | X | - | 테마명으로 필터 (부분 일치) |
| favoriteOnly | boolean | X | false | true일 경우 즐겨찾기한 종목만 조회 (인증 필요) |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| stocks | array | 종목 목록 |
| page | int | 현재 페이지 |
| size | int | 페이지 크기 |
| totalElements | long | 전체 항목 수 |
| totalPages | int | 전체 페이지 수 |

### Stock Object

| Field | Type | Description |
|-------|------|-------------|
| id | int | 종목 ID |
| stockCode | string | 종목코드 |
| stockName | string | 종목명 |
| companySummary | string | 회사 요약 (nullable) |
| themes | array | 연결된 테마 목록 |

### Theme Object (in Stock)

| Field | Type | Description |
|-------|------|-------------|
| id | int | 테마 ID |
| themeName | string | 테마명 |

### Example Request

```
GET /member-api/api/stocks?page=0&size=10
GET /member-api/api/stocks?page=0&size=10&stockName=삼성
GET /member-api/api/stocks?page=0&size=10&stockCode=005930
GET /member-api/api/stocks?page=0&size=10&themeName=반도체
GET /member-api/api/stocks?page=0&size=10&favoriteOnly=true
Authorization: Bearer {token}
```

### Example Response

```json
{
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
    },
    {
      "id": 2,
      "stockCode": "373220",
      "stockName": "LG에너지솔루션",
      "companySummary": "2차전지 제조",
      "themes": [
        { "id": 3, "themeName": "2차전지" }
      ]
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 100,
  "totalPages": 10
}
```

---

## GET /{id}

> 종목 상세 조회 (ID로 조회)

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | int | O | 종목 ID |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| id | int | 종목 ID |
| stockCode | string | 종목코드 |
| stockName | string | 종목명 |
| companySummary | string | 회사 요약 (nullable) |
| themes | array | 연결된 테마 목록 |

### Response `404 Not Found`

종목을 찾을 수 없는 경우

### Example Request

```
GET /member-api/api/stocks/1
```

### Example Response

```json
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
```

---

## GET /code/{stockCode}

> 종목 상세 조회 (종목코드로 조회)

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| stockCode | string | O | 종목코드 |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| id | int | 종목 ID |
| stockCode | string | 종목코드 |
| stockName | string | 종목명 |
| companySummary | string | 회사 요약 (nullable) |
| themes | array | 연결된 테마 목록 |

### Response `404 Not Found`

종목을 찾을 수 없는 경우

### Example Request

```
GET /member-api/api/stocks/code/005930
```

### Example Response

```json
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
```

---

## POST /{stockId}/themes

> 종목에 테마 추가

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| stockId | int | O | 종목 ID |

### Request Body

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| themeIds | array[int] | O | 추가할 테마 ID 목록 |

### Example Request

```
POST /member-api/api/stocks/1/themes
Content-Type: application/json

{
  "themeIds": [1, 2, 3]
}
```

### Response `200 OK`

성공 시 빈 응답

### Response `400 Bad Request`

종목을 찾을 수 없는 경우

---

## DELETE /{stockId}/themes/{themeId}

> 종목에서 테마 제거

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| stockId | int | O | 종목 ID |
| themeId | int | O | 제거할 테마 ID |

### Example Request

```
DELETE /member-api/api/stocks/1/themes/2
```

### Response `204 No Content`

성공 시 빈 응답

### Response `400 Bad Request`

종목을 찾을 수 없는 경우
