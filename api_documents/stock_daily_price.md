# Stock Daily Price API

**Base URL**: `http://localhost:8081/member-api/api/stocks`

---

## API 목록

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/{stockId}/daily-prices` | 종목 ID로 일별 시세 조회 | 필요 |
| GET | `/code/{stockCode}/daily-prices` | 종목코드로 일별 시세 조회 | 필요 |

---

## GET /{stockId}/daily-prices

> 종목 ID로 일별 시세 조회 (페이징, 날짜 필터 지원)

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| stockId | int | O | 종목 ID |

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| startDate | string | X | - | 조회 시작일 (yyyy-MM-dd) |
| endDate | string | X | - | 조회 종료일 (yyyy-MM-dd) |
| page | int | X | 0 | 페이지 번호 (0부터 시작) |
| size | int | X | 30 | 페이지 당 항목 수 |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| prices | array | 일별 시세 목록 |
| stockId | int | 종목 ID |
| stockCode | string | 종목코드 |
| stockName | string | 종목명 |
| page | int | 현재 페이지 |
| size | int | 페이지 크기 |
| totalElements | long | 전체 항목 수 |
| totalPages | int | 전체 페이지 수 |

### StockDailyPrice Object

| Field | Type | Description |
|-------|------|-------------|
| tradeDate | string | 거래일 (yyyy-MM-dd) |
| openPrice | long | 시가 (원, nullable) |
| highPrice | long | 고가 (원, nullable) |
| lowPrice | long | 저가 (원, nullable) |
| closePrice | long | 종가 (원, nullable) |
| volume | long | 거래량 (주, nullable) |
| tradingValue | long | 거래대금 (원, nullable) |
| changeRate | number | 등락률 (%, nullable) |

### Example Request

```
GET /member-api/api/stocks/1/daily-prices?page=0&size=30
GET /member-api/api/stocks/1/daily-prices?startDate=2026-01-01&endDate=2026-02-28
GET /member-api/api/stocks/1/daily-prices?startDate=2026-01-01&endDate=2026-02-28&page=0&size=10
Authorization: Bearer {token}
```

### Example Response

```json
{
  "prices": [
    {
      "tradeDate": "2026-03-04",
      "openPrice": 72000,
      "highPrice": 73500,
      "lowPrice": 71800,
      "closePrice": 73200,
      "volume": 15234567,
      "tradingValue": 1105678900000,
      "changeRate": 1.6620
    },
    {
      "tradeDate": "2026-03-03",
      "openPrice": 71500,
      "highPrice": 72200,
      "lowPrice": 71000,
      "closePrice": 72000,
      "volume": 12345678,
      "tradingValue": 887654321000,
      "changeRate": 0.7000
    }
  ],
  "stockId": 1,
  "stockCode": "005930",
  "stockName": "삼성전자",
  "page": 0,
  "size": 30,
  "totalElements": 250,
  "totalPages": 9
}
```

---

## GET /code/{stockCode}/daily-prices

> 종목코드로 일별 시세 조회 (페이징, 날짜 필터 지원)

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| stockCode | string | O | 종목코드 (예: 005930) |

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| startDate | string | X | - | 조회 시작일 (yyyy-MM-dd) |
| endDate | string | X | - | 조회 종료일 (yyyy-MM-dd) |
| page | int | X | 0 | 페이지 번호 (0부터 시작) |
| size | int | X | 30 | 페이지 당 항목 수 |

### Response `200 OK`

응답 형식은 `GET /{stockId}/daily-prices`와 동일

### Response `404 Not Found`

종목코드로 종목을 찾을 수 없는 경우

### Example Request

```
GET /member-api/api/stocks/code/005930/daily-prices?page=0&size=30
GET /member-api/api/stocks/code/005930/daily-prices?startDate=2026-01-01&endDate=2026-02-28
Authorization: Bearer {token}
```
