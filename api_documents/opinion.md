# Opinion API

**Base URL**: `http://localhost:8081/member-api`

---

## API 목록

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/daily-top30-records/{recordId}/opinions` | 상한가 레코드에 의견 작성 | 필요 |
| GET | `/api/daily-top30-records/{recordId}/opinions` | 특정 상한가 레코드의 의견 목록 | 필요 |
| GET | `/api/opinions/records` | 전체 상한가 의견 목록 | 필요 |
| POST | `/api/news/{newsId}/opinions` | 뉴스에 의견 작성 | 필요 |
| GET | `/api/news/{newsId}/opinions` | 특정 뉴스의 의견 목록 | 필요 |
| GET | `/api/opinions/news` | 전체 뉴스 의견 목록 | 필요 |
| PUT | `/api/daily-top30-records/{recordId}/opinions/{opinionId}` | 상한가 의견 수정 | 필요 |
| PUT | `/api/news/{newsId}/opinions/{opinionId}` | 뉴스 의견 수정 | 필요 |

---

## POST /api/daily-top30-records/{recordId}/opinions

> 상한가 레코드에 의견 작성

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| recordId | int | O | 상한가 레코드 ID |

### Request Body

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| title | string | O | 의견 제목 (최대 200자) |
| content | string | O | 의견 내용 |

### Response `201 Created`

| Field | Type | Description |
|-------|------|-------------|
| id | int | 의견 ID |
| title | string | 제목 |
| content | string | 내용 |
| memberName | string | 작성자 이름 |
| createdAt | datetime | 생성일시 |
| targetType | string | 대상 타입 (RECORD) |
| targetId | int | 대상 ID |

### Example Request

```
POST /member-api/api/daily-top30-records/1/opinions
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "title": "삼성전자 상한가 분석",
  "content": "반도체 수출 호조로 인한 상한가로 보입니다."
}
```

### Example Response

```json
{
  "id": 1,
  "title": "삼성전자 상한가 분석",
  "content": "반도체 수출 호조로 인한 상한가로 보입니다.",
  "memberName": "홍길동",
  "createdAt": "2026-02-14T10:30:00",
  "targetType": "RECORD",
  "targetId": 1
}
```

---

## GET /api/daily-top30-records/{recordId}/opinions

> 특정 상한가 레코드의 의견 목록 조회 (페이징 지원)

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| recordId | int | O | 상한가 레코드 ID |

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | int | X | 0 | 페이지 번호 (0부터 시작) |
| size | int | X | 20 | 페이지 당 항목 수 |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| opinions | array | 의견 목록 |
| page | int | 현재 페이지 |
| size | int | 페이지 크기 |
| totalElements | long | 전체 항목 수 |
| totalPages | int | 전체 페이지 수 |

### Opinion Object

| Field | Type | Description |
|-------|------|-------------|
| id | int | 의견 ID |
| title | string | 제목 |
| content | string | 내용 |
| memberName | string | 작성자 이름 |
| createdAt | datetime | 생성일시 |
| targetType | string | 대상 타입 (RECORD / NEWS) |
| targetId | int | 대상 ID |
| target | object | 연결된 대상 데이터 (nullable) |

### Target Object

| Field | Type | Description |
|-------|------|-------------|
| targetType | string | 대상 타입 (RECORD / NEWS) |
| news | object | 뉴스 정보 (targetType이 NEWS일 때) |
| record | object | 상한가 레코드 정보 (targetType이 RECORD일 때) |

### Target - News Object

| Field | Type | Description |
|-------|------|-------------|
| id | int | 뉴스 ID |
| title | string | 뉴스 제목 |
| link | string | 뉴스 링크 URL |
| press | object | 언론사 정보 (nullable) |
| createdAt | datetime | 생성일시 |

### Target - Record Object

| Field | Type | Description |
|-------|------|-------------|
| id | int | 레코드 ID |
| recordDate | date | 기록 날짜 |
| rank | int | 순위 |
| changeRate | string | 등락률 (nullable) |
| stock | object | 종목 정보 (nullable) |
| themes | array | 테마 목록 |

### Example Request

```
GET /member-api/api/daily-top30-records/1/opinions?page=0&size=10
Authorization: Bearer {accessToken}
```

### Example Response

```json
{
  "opinions": [
    {
      "id": 1,
      "title": "삼성전자 상한가 분석",
      "content": "반도체 수출 호조로 인한 상한가로 보입니다.",
      "memberName": "홍길동",
      "createdAt": "2026-02-14T10:30:00",
      "targetType": "RECORD",
      "targetId": 1,
      "target": {
        "targetType": "RECORD",
        "news": null,
        "record": {
          "id": 1,
          "recordDate": "2026-02-14",
          "rank": 1,
          "changeRate": "+29.95%",
          "stock": {
            "id": 1,
            "stockCode": "005930",
            "stockName": "삼성전자",
            "companySummary": "반도체 제조업체"
          },
          "themes": [
            { "id": 1, "themeName": "반도체" }
          ]
        }
      }
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```

---

## GET /api/opinions/records

> 전체 상한가 의견 목록 조회 (페이징 지원)

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | int | X | 0 | 페이지 번호 (0부터 시작) |
| size | int | X | 20 | 페이지 당 항목 수 |

### Response `200 OK`

응답 형식은 위의 의견 목록 응답과 동일합니다.

### Example Request

```
GET /member-api/api/opinions/records?page=0&size=10
Authorization: Bearer {accessToken}
```

---

## POST /api/news/{newsId}/opinions

> 뉴스에 의견 작성

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| newsId | int | O | 뉴스 ID |

### Request Body

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| title | string | O | 의견 제목 (최대 200자) |
| content | string | O | 의견 내용 |

### Response `201 Created`

응답 형식은 상한가 의견 작성 응답과 동일합니다 (targetType은 NEWS).

### Example Request

```
POST /member-api/api/news/1/opinions
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "title": "뉴스 관련 의견",
  "content": "이 뉴스에 대한 제 의견입니다."
}
```

### Example Response

```json
{
  "id": 2,
  "title": "뉴스 관련 의견",
  "content": "이 뉴스에 대한 제 의견입니다.",
  "memberName": "홍길동",
  "createdAt": "2026-02-14T11:00:00",
  "targetType": "NEWS",
  "targetId": 1
}
```

---

## GET /api/news/{newsId}/opinions

> 특정 뉴스의 의견 목록 조회 (페이징 지원)

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| newsId | int | O | 뉴스 ID |

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | int | X | 0 | 페이지 번호 (0부터 시작) |
| size | int | X | 20 | 페이지 당 항목 수 |

### Response `200 OK`

응답 형식은 위의 의견 목록 응답과 동일합니다.

### Example Request

```
GET /member-api/api/news/1/opinions?page=0&size=10
Authorization: Bearer {accessToken}
```

---

## GET /api/opinions/news

> 전체 뉴스 의견 목록 조회 (페이징 지원)

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | int | X | 0 | 페이지 번호 (0부터 시작) |
| size | int | X | 20 | 페이지 당 항목 수 |

### Response `200 OK`

응답 형식은 위의 의견 목록 응답과 동일합니다.

### Example Request

```
GET /member-api/api/opinions/news?page=0&size=10
Authorization: Bearer {accessToken}
```

---

## PUT /api/daily-top30-records/{recordId}/opinions/{opinionId}

> 상한가 레코드 의견 수정 (본인 의견만 수정 가능)

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| recordId | int | O | 상한가 레코드 ID |
| opinionId | int | O | 의견 ID |

### Request Body

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| title | string | O | 수정할 제목 (최대 200자) |
| content | string | O | 수정할 내용 |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| id | int | 의견 ID |
| title | string | 수정된 제목 |
| content | string | 수정된 내용 |
| memberName | string | 작성자 이름 |
| createdAt | datetime | 생성일시 |
| targetType | string | 대상 타입 (RECORD) |
| targetId | int | 대상 ID |

### Example Request

```
PUT /member-api/api/daily-top30-records/1/opinions/1
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "title": "수정된 제목",
  "content": "수정된 내용입니다."
}
```

### Example Response

```json
{
  "id": 1,
  "title": "수정된 제목",
  "content": "수정된 내용입니다.",
  "memberName": "홍길동",
  "createdAt": "2026-02-14T10:30:00",
  "targetType": "RECORD",
  "targetId": 1
}
```

---

## PUT /api/news/{newsId}/opinions/{opinionId}

> 뉴스 의견 수정 (본인 의견만 수정 가능)

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| newsId | int | O | 뉴스 ID |
| opinionId | int | O | 의견 ID |

### Request Body

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| title | string | O | 수정할 제목 (최대 200자) |
| content | string | O | 수정할 내용 |

### Response `200 OK`

응답 형식은 상한가 의견 수정 응답과 동일합니다 (targetType은 NEWS).

### Example Request

```
PUT /member-api/api/news/1/opinions/2
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "title": "수정된 뉴스 의견",
  "content": "수정된 뉴스 의견 내용입니다."
}
```
