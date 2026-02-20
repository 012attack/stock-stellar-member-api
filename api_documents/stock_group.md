# Stock Group API

**Base URL**: `http://localhost:8081/member-api/api/stock-groups`

---

## API 목록

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/` | 종목 그룹 생성 | 필요 |
| PUT | `/{id}` | 종목 그룹 수정 | 필요 |
| DELETE | `/{id}` | 종목 그룹 삭제 | 필요 |
| GET | `/` | 종목 그룹 목록 조회 | 필요 |
| GET | `/{id}` | 종목 그룹 상세 조회 | 필요 |
| POST | `/{groupId}/stocks` | 그룹에 종목 추가 | 필요 |
| DELETE | `/{groupId}/stocks/{stockId}` | 그룹에서 종목 제거 | 필요 |

> 모든 API는 인증된 사용자 기반으로 본인의 그룹만 조회/수정/삭제 가능합니다.

---

## POST /

> 종목 그룹 생성

### Request Body

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| title | string | O | 그룹 제목 (최대 200자) |
| description | string | X | 그룹 설명 |

### Response `201 Created`

| Field | Type | Description |
|-------|------|-------------|
| id | int | 그룹 ID |
| title | string | 그룹 제목 |
| description | string | 그룹 설명 (nullable) |
| stockCount | int | 포함된 종목 수 |
| createdAt | string | 생성일시 |
| updatedAt | string | 수정일시 |

### Example Request

```
POST /member-api/api/stock-groups
Content-Type: application/json
Authorization: Bearer {token}

{
  "title": "반도체 관련주",
  "description": "반도체 관련 종목 모음"
}
```

### Example Response

```json
{
  "id": 1,
  "title": "반도체 관련주",
  "description": "반도체 관련 종목 모음",
  "stockCount": 0,
  "createdAt": "2026-02-16T12:00:00",
  "updatedAt": "2026-02-16T12:00:00"
}
```

---

## PUT /{id}

> 종목 그룹 수정

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | int | O | 그룹 ID |

### Request Body

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| title | string | O | 그룹 제목 (최대 200자) |
| description | string | X | 그룹 설명 |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| id | int | 그룹 ID |
| title | string | 그룹 제목 |
| description | string | 그룹 설명 (nullable) |
| stockCount | int | 포함된 종목 수 |
| createdAt | string | 생성일시 |
| updatedAt | string | 수정일시 |

### Response `400 Bad Request`

그룹을 찾을 수 없거나 권한이 없는 경우

### Example Request

```
PUT /member-api/api/stock-groups/1
Content-Type: application/json
Authorization: Bearer {token}

{
  "title": "반도체 + AI 관련주",
  "description": "반도체와 AI 관련 종목 모음"
}
```

### Example Response

```json
{
  "id": 1,
  "title": "반도체 + AI 관련주",
  "description": "반도체와 AI 관련 종목 모음",
  "stockCount": 3,
  "createdAt": "2026-02-16T12:00:00",
  "updatedAt": "2026-02-16T12:30:00"
}
```

---

## DELETE /{id}

> 종목 그룹 삭제

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | int | O | 그룹 ID |

### Response `204 No Content`

성공 시 빈 응답

### Response `400 Bad Request`

그룹을 찾을 수 없거나 권한이 없는 경우

### Example Request

```
DELETE /member-api/api/stock-groups/1
Authorization: Bearer {token}
```

---

## GET /

> 종목 그룹 목록 조회 (페이징 지원, 본인 그룹만 조회)

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | int | X | 0 | 페이지 번호 (0부터 시작) |
| size | int | X | 20 | 페이지 당 항목 수 |
| favoriteOnly | boolean | X | false | true일 경우 즐겨찾기한 종목 그룹만 조회 |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| stockGroups | array | 종목 그룹 목록 |
| page | int | 현재 페이지 |
| size | int | 페이지 크기 |
| totalElements | long | 전체 항목 수 |
| totalPages | int | 전체 페이지 수 |

### StockGroup Object (목록)

| Field | Type | Description |
|-------|------|-------------|
| id | int | 그룹 ID |
| title | string | 그룹 제목 |
| description | string | 그룹 설명 (nullable) |
| stockCount | int | 포함된 종목 수 |
| createdAt | string | 생성일시 |
| updatedAt | string | 수정일시 |

### Example Request

```
GET /member-api/api/stock-groups?page=0&size=10
Authorization: Bearer {token}

GET /member-api/api/stock-groups?page=0&size=10&favoriteOnly=true
Authorization: Bearer {token}
```

### Example Response

```json
{
  "stockGroups": [
    {
      "id": 1,
      "title": "반도체 관련주",
      "description": "반도체 관련 종목 모음",
      "stockCount": 5,
      "createdAt": "2026-02-16T12:00:00",
      "updatedAt": "2026-02-16T12:00:00"
    },
    {
      "id": 2,
      "title": "2차전지 관련주",
      "description": null,
      "stockCount": 3,
      "createdAt": "2026-02-16T13:00:00",
      "updatedAt": "2026-02-16T13:00:00"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 2,
  "totalPages": 1
}
```

---

## GET /{id}

> 종목 그룹 상세 조회 (포함된 종목 목록 포함)

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | int | O | 그룹 ID |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| id | int | 그룹 ID |
| title | string | 그룹 제목 |
| description | string | 그룹 설명 (nullable) |
| stocks | array | 포함된 종목 목록 |
| createdAt | string | 생성일시 |
| updatedAt | string | 수정일시 |

### Stock Object (in StockGroup)

| Field | Type | Description |
|-------|------|-------------|
| id | int | 종목 ID |
| stockCode | string | 종목코드 |
| stockName | string | 종목명 |
| companySummary | string | 회사 요약 (nullable) |
| themes | array | 연결된 테마 목록 |

### Response `400 Bad Request`

그룹을 찾을 수 없거나 권한이 없는 경우

### Example Request

```
GET /member-api/api/stock-groups/1
Authorization: Bearer {token}
```

### Example Response

```json
{
  "id": 1,
  "title": "반도체 관련주",
  "description": "반도체 관련 종목 모음",
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
      "stockCode": "000660",
      "stockName": "SK하이닉스",
      "companySummary": "메모리 반도체 제조",
      "themes": [
        { "id": 1, "themeName": "반도체" }
      ]
    }
  ],
  "createdAt": "2026-02-16T12:00:00",
  "updatedAt": "2026-02-16T12:00:00"
}
```

---

## POST /{groupId}/stocks

> 그룹에 종목 추가

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| groupId | int | O | 그룹 ID |

### Request Body

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| stockIds | array[int] | O | 추가할 종목 ID 목록 |

### Response `200 OK`

성공 시 빈 응답

### Response `400 Bad Request`

그룹을 찾을 수 없거나 권한이 없는 경우

### Example Request

```
POST /member-api/api/stock-groups/1/stocks
Content-Type: application/json
Authorization: Bearer {token}

{
  "stockIds": [1, 2, 3]
}
```

---

## DELETE /{groupId}/stocks/{stockId}

> 그룹에서 종목 제거

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| groupId | int | O | 그룹 ID |
| stockId | int | O | 제거할 종목 ID |

### Response `204 No Content`

성공 시 빈 응답

### Response `400 Bad Request`

그룹을 찾을 수 없거나 권한이 없는 경우

### Example Request

```
DELETE /member-api/api/stock-groups/1/stocks/2
Authorization: Bearer {token}
```
