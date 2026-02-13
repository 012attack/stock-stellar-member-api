# Financial Statement API (재무제표)

**Base URL**: `http://localhost:8081/member-api/api/financial-statements`

---

## API 목록

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/` | 재무제표 목록 조회 | 필요 |

---

## GET /

> 재무제표 목록 조회 (페이징, 검색 필터 지원)

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | int | X | 0 | 페이지 번호 (0부터 시작) |
| size | int | X | 20 | 페이지 당 항목 수 |
| stockName | string | X | - | 종목명으로 검색 (부분 일치) |
| bsnsYear | string | X | - | 사업연도 (예: 2024) |
| reprtCode | string | X | - | 보고서 코드 (11011: 사업보고서, 11012: 반기보고서, 11013: 1분기보고서, 11014: 3분기보고서) |
| fsDiv | string | X | - | 재무제표 구분 (OFS: 재무제표, CFS: 연결재무제표) |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| financialStatements | array | 재무제표 목록 |
| page | int | 현재 페이지 |
| size | int | 페이지 크기 |
| totalElements | long | 전체 항목 수 |
| totalPages | int | 전체 페이지 수 |

### FinancialStatement Object

| Field | Type | Description |
|-------|------|-------------|
| id | int | 재무제표 ID |
| stock | object | 종목 정보 (nullable) |
| rceptNo | string | 접수번호 |
| reprtCode | string | 보고서 코드 |
| bsnsYear | string | 사업연도 |
| corpCode | string | 고유번호 (nullable) |
| fsDiv | string | 재무제표 구분 (OFS/CFS) |
| sjDiv | string | 재무제표 종류 (BS: 재무상태표, IS: 손익계산서, CIS: 포괄손익계산서, CF: 현금흐름표, SCE: 자본변동표) |
| sjNm | string | 재무제표 종류명 (nullable) |
| accountId | string | 계정 ID (nullable) |
| accountNm | string | 계정명 |
| accountDetail | string | 계정 상세 (nullable) |
| thstrmNm | string | 당기명 (nullable) |
| thstrmAmount | long | 당기금액 (nullable) |
| thstrmAddAmount | long | 당기누적금액 (nullable) |
| frmtrmNm | string | 전기명 (nullable) |
| frmtrmAmount | long | 전기금액 (nullable) |
| frmtrmQNm | string | 전기분기명 (nullable) |
| frmtrmQAmount | long | 전기분기금액 (nullable) |
| frmtrmAddAmount | long | 전기누적금액 (nullable) |
| bfefrmtrmNm | string | 전전기명 (nullable) |
| bfefrmtrmAmount | long | 전전기금액 (nullable) |
| ord | int | 정렬순서 (nullable) |
| currency | string | 통화 (nullable) |
| createdAt | datetime | 생성일시 (nullable) |

### Stock Object (nested)

| Field | Type | Description |
|-------|------|-------------|
| id | int | 종목 ID |
| stockCode | string | 종목코드 |
| stockName | string | 종목명 |
| companySummary | string | 회사 요약 (nullable) |

### Example Request

```
GET /member-api/api/financial-statements?page=0&size=10
GET /member-api/api/financial-statements?page=0&size=10&stockName=삼성
GET /member-api/api/financial-statements?page=0&size=10&bsnsYear=2024&reprtCode=11011
GET /member-api/api/financial-statements?page=0&size=10&fsDiv=CFS
```

### Example Response

```json
{
  "financialStatements": [
    {
      "id": 1,
      "stock": {
        "id": 1,
        "stockCode": "005930",
        "stockName": "삼성전자",
        "companySummary": "반도체 및 전자제품 제조"
      },
      "rceptNo": "20240315000001",
      "reprtCode": "11011",
      "bsnsYear": "2024",
      "corpCode": "00126380",
      "fsDiv": "CFS",
      "sjDiv": "BS",
      "sjNm": "재무상태표",
      "accountId": "ifrs-full_Assets",
      "accountNm": "자산총계",
      "accountDetail": null,
      "thstrmNm": "제56기",
      "thstrmAmount": 455905800000000,
      "thstrmAddAmount": null,
      "frmtrmNm": "제55기",
      "frmtrmAmount": 426612800000000,
      "frmtrmQNm": null,
      "frmtrmQAmount": null,
      "frmtrmAddAmount": null,
      "bfefrmtrmNm": "제54기",
      "bfefrmtrmAmount": 448261700000000,
      "ord": 1,
      "currency": "KRW",
      "createdAt": "2024-03-15T10:30:00"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 500,
  "totalPages": 50
}
```
