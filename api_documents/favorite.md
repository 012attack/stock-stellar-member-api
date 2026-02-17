# 즐겨찾기 (Favorite) API

## 개요
stock, stock_group, 상한가 기록(record), 뉴스(news), 테마(theme), 의견(opinion) 6가지 대상에 대한 즐겨찾기 기능.
모든 API는 인증이 필요합니다.

## Target Type

| 값 | 설명 |
|---|------|
| `STOCK` | 종목 |
| `STOCK_GROUP` | 종목 그룹 |
| `RECORD` | 상한가 기록 |
| `NEWS` | 뉴스 |
| `THEME` | 테마 |
| `OPINION` | 의견 |

---

## API 목록

### 1. 즐겨찾기 추가

| 항목 | 내용 |
|------|------|
| **Method** | `POST` |
| **URL** | `/member-api/api/favorites` |
| **Auth** | Required |
| **Content-Type** | `application/json` |

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `targetType` | String | O | 대상 타입 (STOCK, STOCK_GROUP, RECORD, NEWS, THEME, OPINION) |
| `targetId` | Integer | O | 대상 ID |

**Request 예시**
```json
{
  "targetType": "STOCK",
  "targetId": 1
}
```

**Response (201 Created)**

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | Integer | 즐겨찾기 ID |
| `targetType` | String | 대상 타입 |
| `targetId` | Integer | 대상 ID |
| `createdAt` | String | 생성 시간 (ISO 8601) |

**Response 예시**
```json
{
  "id": 1,
  "targetType": "STOCK",
  "targetId": 1,
  "createdAt": "2026-02-17T13:00:00"
}
```

**에러**
| 상태 코드 | 설명 |
|-----------|------|
| 400 | 이미 즐겨찾기에 추가된 경우 |
| 401 | 인증 실패 |

---

### 2. 즐겨찾기 제거

| 항목 | 내용 |
|------|------|
| **Method** | `DELETE` |
| **URL** | `/member-api/api/favorites?targetType={targetType}&targetId={targetId}` |
| **Auth** | Required |

**Query Parameters**

| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `targetType` | String | O | 대상 타입 |
| `targetId` | Integer | O | 대상 ID |

**Response (204 No Content)**

본문 없음. 이미 삭제된 경우에도 204를 반환합니다 (멱등성).

---

### 3. 즐겨찾기 목록 조회 (페이징)

| 항목 | 내용 |
|------|------|
| **Method** | `GET` |
| **URL** | `/member-api/api/favorites?targetType={targetType}&page={page}&size={size}` |
| **Auth** | Required |

**Query Parameters**

| 파라미터 | 타입 | 필수 | 기본값 | 설명 |
|----------|------|------|--------|------|
| `targetType` | String | O | - | 대상 타입 |
| `page` | Integer | X | 0 | 페이지 번호 (0부터 시작) |
| `size` | Integer | X | 20 | 페이지 크기 |

**Response (200 OK)**

| 필드 | 타입 | 설명 |
|------|------|------|
| `favorites` | Array | 즐겨찾기 목록 |
| `favorites[].id` | Integer | 즐겨찾기 ID |
| `favorites[].targetType` | String | 대상 타입 |
| `favorites[].targetId` | Integer | 대상 ID |
| `favorites[].createdAt` | String | 생성 시간 |
| `page` | Integer | 현재 페이지 |
| `size` | Integer | 페이지 크기 |
| `totalElements` | Long | 총 항목 수 |
| `totalPages` | Integer | 총 페이지 수 |

**Response 예시**
```json
{
  "favorites": [
    {
      "id": 2,
      "targetType": "STOCK",
      "targetId": 5,
      "createdAt": "2026-02-17T14:00:00"
    },
    {
      "id": 1,
      "targetType": "STOCK",
      "targetId": 1,
      "createdAt": "2026-02-17T13:00:00"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 2,
  "totalPages": 1
}
```

---

### 4. 즐겨찾기 여부 확인

| 항목 | 내용 |
|------|------|
| **Method** | `GET` |
| **URL** | `/member-api/api/favorites/check?targetType={targetType}&targetId={targetId}` |
| **Auth** | Required |

**Query Parameters**

| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `targetType` | String | O | 대상 타입 |
| `targetId` | Integer | O | 대상 ID |

**Response (200 OK)**

| 필드 | 타입 | 설명 |
|------|------|------|
| `targetType` | String | 대상 타입 |
| `targetId` | Integer | 대상 ID |
| `favorited` | Boolean | 즐겨찾기 여부 |

**Response 예시**
```json
{
  "targetType": "STOCK",
  "targetId": 1,
  "favorited": true
}
```
