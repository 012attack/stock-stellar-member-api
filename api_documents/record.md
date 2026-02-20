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
> - startDate, endDate를 전달하면 날짜 범위로 조회
> - startDate, endDate를 전달하지 않으면 페이징으로 조회

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| startDate | string | X | - | 시작 날짜 (yyyy-MM-dd 형식). endDate와 함께 사용 |
| endDate | string | X | - | 끝 날짜 (yyyy-MM-dd 형식). startDate와 함께 사용 |
| page | int | X | 0 | 페이지 번호 (날짜 미전달 시 사용) |
| size | int | X | 20 | 페이지 크기 (날짜 미전달 시 사용) |
| stockName | string | X | - | 종목명으로 필터링 (부분 일치) |
| stockCode | string | X | - | 종목코드로 필터링 (부분 일치) |
| themeName | string | X | - | 테마명으로 필터링 (부분 일치) |
| favoriteOnly | boolean | X | false | true일 경우 즐겨찾기한 기록만 조회 (인증 필요) |

### Response `200 OK` (날짜 범위 조회 시)

| Field | Type | Description |
|-------|------|-------------|
| records | array | Daily Top 30 기록 목록 |
| startDate | string | 조회 시작 날짜 |
| endDate | string | 조회 끝 날짜 |

### Response `200 OK` (페이징 조회 시)

| Field | Type | Description |
|-------|------|-------------|
| records | array | Daily Top 30 기록 목록 |
| page | int | 현재 페이지 번호 |
| size | int | 페이지 크기 |
| totalElements | long | 전체 데이터 수 |
| totalPages | int | 전체 페이지 수 |

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
# 날짜 범위 조회
GET /member-api/api/daily-top30-records?startDate=2026-02-01&endDate=2026-02-04
GET /member-api/api/daily-top30-records?startDate=2026-02-01&endDate=2026-02-04&stockName=삼성
GET /member-api/api/daily-top30-records?startDate=2026-02-04&endDate=2026-02-04&stockCode=005930
GET /member-api/api/daily-top30-records?startDate=2026-01-01&endDate=2026-02-04&themeName=반도체

# 페이징 조회 (날짜 미전달)
GET /member-api/api/daily-top30-records
GET /member-api/api/daily-top30-records?page=0&size=20
GET /member-api/api/daily-top30-records?page=1&size=10&stockName=삼성

# 즐겨찾기 조회
GET /member-api/api/daily-top30-records?favoriteOnly=true
Authorization: Bearer {token}
```

### Example Response (날짜 범위 조회)

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
    }
  ],
  "startDate": "2026-02-01",
  "endDate": "2026-02-04"
}
```

### Example Response (페이징 조회)

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
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 150,
  "totalPages": 8
}
```
