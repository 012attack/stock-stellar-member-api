# Investment Case API (투자 성공/실패 사례)

투자 성공/실패 사례를 관리하는 API입니다. 제목, HTML 컨텐츠, 성공/실패 구분으로 구성됩니다.

## Base URL

`/member-api/api/investment-cases`

## Endpoints

| Method | URL | 설명 | 인증 |
|--------|-----|------|------|
| POST | `/api/investment-cases` | 사례 등록 | 필요 |
| GET | `/api/investment-cases` | 사례 목록 조회 | 불필요 |
| GET | `/api/investment-cases/{id}` | 사례 상세 조회 | 불필요 |
| PUT | `/api/investment-cases/{id}` | 사례 수정 | 필요 |
| DELETE | `/api/investment-cases/{id}` | 사례 삭제 | 필요 |

---

## 1. 사례 등록

**POST** `/api/investment-cases`

### Request Body

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| title | String | O | 사례 제목 (최대 200자) |
| content | String | O | 사례 내용 (HTML) |
| resultType | String | O | 결과 유형 (SUCCESS / FAILURE) |

```json
{
  "title": "테슬라 저점 매수 성공",
  "content": "<p>2025년 3월 테슬라 주가 하락 시 분할 매수하여 40% 수익 실현</p>",
  "resultType": "SUCCESS"
}
```

### Response (201 Created)

```json
{
  "id": 1,
  "title": "테슬라 저점 매수 성공",
  "content": "<p>2025년 3월 테슬라 주가 하락 시 분할 매수하여 40% 수익 실현</p>",
  "resultType": "SUCCESS",
  "createdAt": "2026-02-25T15:00:00",
  "updatedAt": "2026-02-25T15:00:00"
}
```

---

## 2. 사례 목록 조회

**GET** `/api/investment-cases`

### Query Parameters

| 파라미터 | 타입 | 기본값 | 설명 |
|----------|------|--------|------|
| page | Int | 0 | 페이지 번호 |
| size | Int | 20 | 페이지 크기 |
| resultType | String | - | 결과 유형 필터 (SUCCESS / FAILURE) |

### Response (200 OK)

```json
{
  "investmentCases": [
    {
      "id": 1,
      "title": "테슬라 저점 매수 성공",
      "content": "<p>2025년 3월 테슬라 주가 하락 시 분할 매수하여 40% 수익 실현</p>",
      "resultType": "SUCCESS",
      "createdAt": "2026-02-25T15:00:00",
      "updatedAt": "2026-02-25T15:00:00"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

---

## 3. 사례 상세 조회

**GET** `/api/investment-cases/{id}`

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| id | Int | 사례 ID |

### Response (200 OK)

```json
{
  "id": 1,
  "title": "테슬라 저점 매수 성공",
  "content": "<p>2025년 3월 테슬라 주가 하락 시 분할 매수하여 40% 수익 실현</p>",
  "resultType": "SUCCESS",
  "memberName": "홍길동",
  "createdAt": "2026-02-25T15:00:00",
  "updatedAt": "2026-02-25T15:00:00"
}
```

---

## 4. 사례 수정

**PUT** `/api/investment-cases/{id}`

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| id | Int | 사례 ID |

### Request Body

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| title | String | O | 사례 제목 (최대 200자) |
| content | String | O | 사례 내용 (HTML) |
| resultType | String | O | 결과 유형 (SUCCESS / FAILURE) |

```json
{
  "title": "테슬라 저점 매수 성공 (수정)",
  "content": "<p>2025년 3월 테슬라 주가 하락 시 분할 매수하여 45% 수익 실현</p>",
  "resultType": "SUCCESS"
}
```

### Response (200 OK)

```json
{
  "id": 1,
  "title": "테슬라 저점 매수 성공 (수정)",
  "content": "<p>2025년 3월 테슬라 주가 하락 시 분할 매수하여 45% 수익 실현</p>",
  "resultType": "SUCCESS",
  "createdAt": "2026-02-25T15:00:00",
  "updatedAt": "2026-02-25T16:00:00"
}
```

---

## 5. 사례 삭제

**DELETE** `/api/investment-cases/{id}`

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| id | Int | 사례 ID |

### Response (204 No Content)
