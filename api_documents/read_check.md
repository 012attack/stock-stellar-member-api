# 조회 확인 (ReadCheck) API

## 개요
상한가 기록(record), 뉴스(news), 의견(opinion) 3가지 대상에 대한 조회 여부 확인 기능.
모든 API는 인증이 필요합니다.

## Target Type

| 값 | 설명 |
|---|------|
| `RECORD` | 상한가 기록 |
| `NEWS` | 뉴스 |
| `OPINION` | 의견 |

---

## API 목록

### 1. 조회 확인 등록 (읽음 표시)

| 항목 | 내용 |
|------|------|
| **Method** | `POST` |
| **URL** | `/member-api/api/read-checks` |
| **Auth** | Required |
| **Content-Type** | `application/json` |

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `targetType` | String | O | 대상 타입 (RECORD, NEWS, OPINION) |
| `targetId` | Integer | O | 대상 ID |

**Request 예시**
```json
{
  "targetType": "RECORD",
  "targetId": 1
}
```

**Response (201 Created)**

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | Integer | 조회 확인 ID |
| `targetType` | String | 대상 타입 |
| `targetId` | Integer | 대상 ID |
| `createdAt` | String | 생성 시간 (ISO 8601) |

**Response 예시**
```json
{
  "id": 1,
  "targetType": "RECORD",
  "targetId": 1,
  "createdAt": "2026-02-20T13:00:00"
}
```

**에러**
| 상태 코드 | 설명 |
|-----------|------|
| 400 | 이미 조회 확인된 항목인 경우 |
| 401 | 인증 실패 |

---

### 2. 조회 확인 제거 (읽음 취소)

| 항목 | 내용 |
|------|------|
| **Method** | `DELETE` |
| **URL** | `/member-api/api/read-checks?targetType={targetType}&targetId={targetId}` |
| **Auth** | Required |

**Query Parameters**

| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `targetType` | String | O | 대상 타입 |
| `targetId` | Integer | O | 대상 ID |

**Response (204 No Content)**

본문 없음. 이미 삭제된 경우에도 204를 반환합니다 (멱등성).

---

### 3. 조회 여부 확인

| 항목 | 내용 |
|------|------|
| **Method** | `GET` |
| **URL** | `/member-api/api/read-checks/check?targetType={targetType}&targetId={targetId}` |
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
| `checked` | Boolean | 조회 여부 |

**Response 예시**
```json
{
  "targetType": "NEWS",
  "targetId": 5,
  "checked": true
}
```
