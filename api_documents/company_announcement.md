# Company Announcement API (공시)

**Base URL**: `http://localhost:8081/member-api/api/company-announcements`

---

## API 목록

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/` | 공시 목록 조회 | 필요 |
| GET | `/{id}` | 공시 상세 조회 | 필요 |

---

## GET /

> 공시 목록 조회 (페이징, 검색 필터 지원)

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | int | X | 0 | 페이지 번호 (0부터 시작) |
| size | int | X | 20 | 페이지 당 항목 수 |
| stockId | int | X | - | 종목 ID 필터 |
| reportNm | string | X | - | 보고서명 검색 (부분 일치) |
| pblntfTy | string | X | - | 공시 유형 필터 (A~J) |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| announcements | array | 공시 목록 |
| page | int | 현재 페이지 |
| size | int | 페이지 크기 |
| totalElements | long | 전체 항목 수 |
| totalPages | int | 전체 페이지 수 |

### Announcement Object

| Field | Type | Description |
|-------|------|-------------|
| id | int | 공시 ID |
| stockId | int | 종목 ID |
| stockName | string | 종목명 |
| stockCode | string | 종목코드 |
| rceptNo | string | 접수번호 |
| corpCode | string | 고유번호 (nullable) |
| reportNm | string | 보고서명 |
| flrNm | string | 공시 제출인명 (nullable) |
| rceptDt | string | 접수일자 (yyyy-MM-dd) |
| pblntfTy | string | 공시유형 (nullable, A~J) |
| pblntfDetailTy | string | 공시상세유형 (nullable) |
| createdAt | string | 생성일시 (nullable) |

### 공시유형 (pblntfTy) 값

| Value | Description |
|-------|-------------|
| A | 정기공시 |
| B | 주요사항보고 |
| C | 발행공시 |
| D | 지분공시 |
| E | 기타공시 |
| F | 외부감사관련 |
| G | 펀드공시 |
| H | 자산유동화 |
| I | 거래소공시 |
| J | 공정위공시 |

### Example Request

```
GET /member-api/api/company-announcements?page=0&size=10
GET /member-api/api/company-announcements?page=0&size=10&stockId=1
GET /member-api/api/company-announcements?page=0&size=10&reportNm=사업보고서
GET /member-api/api/company-announcements?page=0&size=10&pblntfTy=A
Authorization: Bearer {token}
```

### Example Response

```json
{
  "announcements": [
    {
      "id": 1,
      "stockId": 1,
      "stockName": "삼성전자",
      "stockCode": "005930",
      "rceptNo": "20260228000001",
      "corpCode": "00126380",
      "reportNm": "사업보고서 (2025.12)",
      "flrNm": "삼성전자",
      "rceptDt": "2026-02-28",
      "pblntfTy": "A",
      "pblntfDetailTy": "A001",
      "createdAt": "2026-02-28T10:00:00"
    },
    {
      "id": 2,
      "stockId": 2,
      "stockName": "SK하이닉스",
      "stockCode": "000660",
      "rceptNo": "20260227000002",
      "corpCode": "00164779",
      "reportNm": "주요사항보고서(유상증자결정)",
      "flrNm": "SK하이닉스",
      "rceptDt": "2026-02-27",
      "pblntfTy": "B",
      "pblntfDetailTy": "B001",
      "createdAt": "2026-02-27T14:30:00"
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

> 공시 상세 조회

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | int | O | 공시 ID |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| id | int | 공시 ID |
| stockId | int | 종목 ID |
| stockName | string | 종목명 |
| stockCode | string | 종목코드 |
| rceptNo | string | 접수번호 |
| corpCode | string | 고유번호 (nullable) |
| reportNm | string | 보고서명 |
| flrNm | string | 공시 제출인명 (nullable) |
| rceptDt | string | 접수일자 (yyyy-MM-dd) |
| pblntfTy | string | 공시유형 (nullable) |
| pblntfDetailTy | string | 공시상세유형 (nullable) |
| createdAt | string | 생성일시 (nullable) |

### Response `404 Not Found`

공시를 찾을 수 없는 경우

### Example Request

```
GET /member-api/api/company-announcements/1
Authorization: Bearer {token}
```

### Example Response

```json
{
  "id": 1,
  "stockId": 1,
  "stockName": "삼성전자",
  "stockCode": "005930",
  "rceptNo": "20260228000001",
  "corpCode": "00126380",
  "reportNm": "사업보고서 (2025.12)",
  "flrNm": "삼성전자",
  "rceptDt": "2026-02-28",
  "pblntfTy": "A",
  "pblntfDetailTy": "A001",
  "createdAt": "2026-02-28T10:00:00"
}
```
