# Importance (중요도/별점) API

## 개요
상한가 기록(RECORD)과 뉴스(NEWS)에 대해 사용자별 중요도(별점)를 관리하는 API입니다.
0~10점, 0.5점 단위로 별점을 매길 수 있습니다.

## Target Types

| Target Type | 설명 |
|-------------|------|
| `RECORD` | 상한가 기록 |
| `NEWS` | 뉴스 |

---

## API 목록

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| PUT | `/api/importances` | 별점 설정/수정 (upsert) | Required |
| GET | `/api/importances?targetType={}&targetId={}` | 별점 조회 | Required |
| DELETE | `/api/importances?targetType={}&targetId={}` | 별점 삭제 | Required |

---

## 1. 별점 설정/수정 (Upsert)

별점이 없으면 새로 생성하고, 이미 있으면 점수를 업데이트합니다.

### Request

```
PUT /member-api/api/importances
Content-Type: application/json
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `targetType` | String | Y | `RECORD` 또는 `NEWS` |
| `targetId` | Integer | Y | 대상 ID |
| `score` | BigDecimal | Y | 별점 (0~10, 0.5 단위) |

### Request Body 예시

```json
{
  "targetType": "RECORD",
  "targetId": 1,
  "score": 7.5
}
```

### Response (200 OK)

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | Integer | 중요도 ID |
| `targetType` | String | 대상 타입 |
| `targetId` | Integer | 대상 ID |
| `score` | BigDecimal | 별점 |
| `createdAt` | String | 생성 일시 |
| `updatedAt` | String | 수정 일시 |

### Response Body 예시

```json
{
  "id": 1,
  "targetType": "RECORD",
  "targetId": 1,
  "score": 7.5,
  "createdAt": "2026-02-22T21:07:00",
  "updatedAt": "2026-02-22T21:07:00"
}
```

---

## 2. 별점 조회

특정 대상에 대한 현재 사용자의 별점을 조회합니다.

### Request

```
GET /member-api/api/importances?targetType={targetType}&targetId={targetId}
```

| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `targetType` | String | Y | `RECORD` 또는 `NEWS` |
| `targetId` | Integer | Y | 대상 ID |

### Response (200 OK)

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | Integer | 중요도 ID |
| `targetType` | String | 대상 타입 |
| `targetId` | Integer | 대상 ID |
| `score` | BigDecimal | 별점 |
| `createdAt` | String | 생성 일시 |
| `updatedAt` | String | 수정 일시 |

### Response Body 예시

```json
{
  "id": 1,
  "targetType": "NEWS",
  "targetId": 5,
  "score": 9.0,
  "createdAt": "2026-02-22T21:07:00",
  "updatedAt": "2026-02-22T21:10:00"
}
```

---

## 3. 별점 삭제

특정 대상에 대한 현재 사용자의 별점을 삭제합니다.

### Request

```
DELETE /member-api/api/importances?targetType={targetType}&targetId={targetId}
```

| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `targetType` | String | Y | `RECORD` 또는 `NEWS` |
| `targetId` | Integer | Y | 대상 ID |

### Response (204 No Content)

응답 본문 없음.

---

## Score 규칙

| 항목 | 값 |
|------|------|
| 최소값 | 0 |
| 최대값 | 10 |
| 단위 | 0.5 |
| 유효 예시 | 0, 0.5, 1.0, 1.5, ... 9.5, 10.0 |
| 무효 예시 | -1, 0.3, 10.5, 11 |
