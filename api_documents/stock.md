# Stock API

**Base URL**: `http://localhost:8081/member-api/api/stocks`

---

## API 목록

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/` | 종목 목록 조회 | 필요 |
| GET | `/{id}` | 종목 상세 조회 (ID) | 필요 |
| GET | `/code/{stockCode}` | 종목 상세 조회 (종목코드) | 필요 |

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

### Example Request

```
GET /member-api/api/stocks?page=0&size=10
GET /member-api/api/stocks?page=0&size=10&stockName=삼성
GET /member-api/api/stocks?page=0&size=10&stockCode=005930
```

### Example Response

```json
{
  "stocks": [
    {
      "id": 1,
      "stockCode": "005930",
      "stockName": "삼성전자",
      "companySummary": "반도체 및 전자제품 제조"
    },
    {
      "id": 2,
      "stockCode": "373220",
      "stockName": "LG에너지솔루션",
      "companySummary": "2차전지 제조"
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
  "companySummary": "반도체 및 전자제품 제조"
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
  "companySummary": "반도체 및 전자제품 제조"
}
```
