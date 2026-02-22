# Stock Market Schedule API (증시 주요일정)

증시 주요일정을 관리하는 API입니다. 날짜, 제목, 컨텐츠(HTML)로 구성됩니다.

## Base URL

`/member-api/api/stock-market-schedules`

## Endpoints

| Method | URL | 설명 | 인증 |
|--------|-----|------|------|
| POST | `/api/stock-market-schedules` | 일정 등록 | 필요 |
| GET | `/api/stock-market-schedules` | 일정 목록 조회 | 필요 |
| GET | `/api/stock-market-schedules/{id}` | 일정 상세 조회 | 필요 |
| PUT | `/api/stock-market-schedules/{id}` | 일정 수정 | 필요 |
| DELETE | `/api/stock-market-schedules/{id}` | 일정 삭제 | 필요 |

---

## 1. 일정 등록

**POST** `/api/stock-market-schedules`

### Request Body

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| title | String | O | 일정 제목 (최대 200자) |
| content | String | X | 일정 내용 (HTML) |
| scheduleDate | LocalDate | O | 일정 날짜 (yyyy-MM-dd) |

```json
{
  "title": "FOMC 금리 결정",
  "content": "<p>미국 연준 FOMC 회의 결과 발표</p>",
  "scheduleDate": "2026-03-15"
}
```

### Response (201 Created)

```json
{
  "id": 1,
  "title": "FOMC 금리 결정",
  "content": "<p>미국 연준 FOMC 회의 결과 발표</p>",
  "scheduleDate": "2026-03-15",
  "createdAt": "2026-02-22T15:00:00",
  "updatedAt": "2026-02-22T15:00:00"
}
```

---

## 2. 일정 목록 조회

**GET** `/api/stock-market-schedules`

### Query Parameters

| 파라미터 | 타입 | 기본값 | 설명 |
|----------|------|--------|------|
| page | Int | 0 | 페이지 번호 |
| size | Int | 20 | 페이지 크기 |
| startDate | LocalDate | - | 조회 시작 날짜 (yyyy-MM-dd) |
| endDate | LocalDate | - | 조회 종료 날짜 (yyyy-MM-dd) |

### Response (200 OK)

```json
{
  "schedules": [
    {
      "id": 1,
      "title": "FOMC 금리 결정",
      "content": "<p>미국 연준 FOMC 회의 결과 발표</p>",
      "scheduleDate": "2026-03-15",
      "createdAt": "2026-02-22T15:00:00",
      "updatedAt": "2026-02-22T15:00:00"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

---

## 3. 일정 상세 조회

**GET** `/api/stock-market-schedules/{id}`

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| id | Int | 일정 ID |

### Response (200 OK)

```json
{
  "id": 1,
  "title": "FOMC 금리 결정",
  "content": "<p>미국 연준 FOMC 회의 결과 발표</p>",
  "scheduleDate": "2026-03-15",
  "memberName": "홍길동",
  "createdAt": "2026-02-22T15:00:00",
  "updatedAt": "2026-02-22T15:00:00"
}
```

---

## 4. 일정 수정

**PUT** `/api/stock-market-schedules/{id}`

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| id | Int | 일정 ID |

### Request Body

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| title | String | O | 일정 제목 (최대 200자) |
| content | String | X | 일정 내용 (HTML) |
| scheduleDate | LocalDate | O | 일정 날짜 (yyyy-MM-dd) |

```json
{
  "title": "FOMC 금리 결정 (수정)",
  "content": "<p>미국 연준 FOMC 회의 결과 발표 - 금리 동결 예상</p>",
  "scheduleDate": "2026-03-15"
}
```

### Response (200 OK)

```json
{
  "id": 1,
  "title": "FOMC 금리 결정 (수정)",
  "content": "<p>미국 연준 FOMC 회의 결과 발표 - 금리 동결 예상</p>",
  "scheduleDate": "2026-03-15",
  "createdAt": "2026-02-22T15:00:00",
  "updatedAt": "2026-02-22T16:00:00"
}
```

---

## 5. 일정 삭제

**DELETE** `/api/stock-market-schedules/{id}`

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| id | Int | 일정 ID |

### Response (204 No Content)
