# Record API

**Base URL**: `http://localhost:8081/member-api/api/daily-top30-records`

---

## API 목록

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/` | Daily Top 30 기록 조회 | 필요 |

---

## GET /

> Daily Top 30 기록 조회 (연관 Stock, Theme 정보 포함)

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| recordDate | string | X | 최신 날짜 | 조회할 날짜 (yyyy-MM-dd 형식) |
| stockName | string | X | - | 종목명으로 필터링 (부분 일치) |
| stockCode | string | X | - | 종목코드로 필터링 (부분 일치) |
| themeName | string | X | - | 테마명으로 필터링 (부분 일치) |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| records | array | Daily Top 30 기록 목록 |
| recordDate | string | 조회된 날짜 |

### DailyTop30Record Object

| Field | Type | Description |
|-------|------|-------------|
| id | int | 기록 ID |
| recordDate | string | 기록 날짜 |
| rank | int | 순위 (1~30) |
| changeRate | string | 변동률 |
| description | string | 설명 |
| createdAt | string | 생성일시 |
| stock | object | 연관 종목 정보 (Stock Object) |
| themes | array | 연관 테마 목록 (Theme Object) |

### Stock Object

| Field | Type | Description |
|-------|------|-------------|
| id | int | 종목 ID |
| stockCode | string | 종목코드 |
| stockName | string | 종목명 |
| companySummary | string | 회사 요약 |

### Theme Object

| Field | Type | Description |
|-------|------|-------------|
| id | int | 테마 ID |
| themeName | string | 테마명 |

### Example Request

```
GET /member-api/api/daily-top30-records?recordDate=2026-02-04
GET /member-api/api/daily-top30-records?recordDate=2026-02-04&stockName=삼성
GET /member-api/api/daily-top30-records?recordDate=2026-02-04&stockCode=005930
GET /member-api/api/daily-top30-records?recordDate=2026-02-04&themeName=반도체
```

### Example Response

```json
{
  "records": [
    {
      "id": 1,
      "recordDate": "2026-02-04",
      "rank": 1,
      "changeRate": "+5.2%",
      "description": "AI 반도체 수요 증가",
      "createdAt": "2026-02-04T10:00:00",
      "stock": {
        "id": 1,
        "stockCode": "005930",
        "stockName": "삼성전자",
        "companySummary": "반도체 및 전자제품 제조"
      },
      "themes": [
        { "id": 1, "themeName": "반도체" },
        { "id": 2, "themeName": "AI" }
      ]
    },
    {
      "id": 2,
      "recordDate": "2026-02-04",
      "rank": 2,
      "changeRate": "+4.8%",
      "description": "2차전지 호재",
      "createdAt": "2026-02-04T10:00:00",
      "stock": {
        "id": 2,
        "stockCode": "373220",
        "stockName": "LG에너지솔루션",
        "companySummary": "2차전지 제조"
      },
      "themes": [
        { "id": 3, "themeName": "2차전지" }
      ]
    }
  ],
  "recordDate": "2026-02-04"
}
```
