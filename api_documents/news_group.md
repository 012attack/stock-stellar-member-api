# News Group API

뉴스 기사들을 그룹으로 묶어서 관리하는 API입니다.

## Base URL

`/member-api/api/news-groups`

## Endpoints

| Method | URL | 설명 | 인증 |
|--------|-----|------|------|
| POST | `/api/news-groups` | 뉴스 그룹 생성 | 필요 |
| GET | `/api/news-groups` | 뉴스 그룹 목록 조회 | 필요 |
| GET | `/api/news-groups/{id}` | 뉴스 그룹 상세 조회 | 필요 |
| PUT | `/api/news-groups/{id}` | 뉴스 그룹 수정 | 필요 |
| DELETE | `/api/news-groups/{id}` | 뉴스 그룹 삭제 | 필요 |
| POST | `/api/news-groups/{groupId}/news` | 그룹에 뉴스 추가 | 필요 |
| DELETE | `/api/news-groups/{groupId}/news/{newsId}` | 그룹에서 뉴스 제거 | 필요 |

---

## 1. 뉴스 그룹 생성

**POST** `/api/news-groups`

### Request Body

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| title | String | O | 그룹 제목 (최대 200자) |
| description | String | X | 그룹 설명 |

```json
{
  "title": "AI 관련 뉴스",
  "description": "인공지능 관련 뉴스 모음"
}
```

### Response (201 Created)

```json
{
  "id": 1,
  "title": "AI 관련 뉴스",
  "description": "인공지능 관련 뉴스 모음",
  "newsCount": 0,
  "createdAt": "2026-02-20T15:00:00",
  "updatedAt": "2026-02-20T15:00:00"
}
```

---

## 2. 뉴스 그룹 목록 조회

**GET** `/api/news-groups`

### Query Parameters

| 파라미터 | 타입 | 기본값 | 설명 |
|----------|------|--------|------|
| page | Int | 0 | 페이지 번호 |
| size | Int | 20 | 페이지 크기 |
| favoriteOnly | Boolean | false | 즐겨찾기 필터 |

### Response (200 OK)

```json
{
  "newsGroups": [
    {
      "id": 1,
      "title": "AI 관련 뉴스",
      "description": "인공지능 관련 뉴스 모음",
      "newsCount": 5,
      "createdAt": "2026-02-20T15:00:00",
      "updatedAt": "2026-02-20T15:00:00"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

---

## 3. 뉴스 그룹 상세 조회

**GET** `/api/news-groups/{id}`

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| id | Int | 뉴스 그룹 ID |

### Response (200 OK)

```json
{
  "id": 1,
  "title": "AI 관련 뉴스",
  "description": "인공지능 관련 뉴스 모음",
  "news": [
    {
      "id": 10,
      "title": "AI 시장 전망",
      "link": "https://example.com/news/10",
      "press": {
        "id": 1,
        "name": "한국경제"
      },
      "themes": [
        {
          "id": 1,
          "themeName": "인공지능"
        }
      ],
      "createdAt": "2026-02-20T10:00:00"
    }
  ],
  "createdAt": "2026-02-20T15:00:00",
  "updatedAt": "2026-02-20T15:00:00"
}
```

---

## 4. 뉴스 그룹 수정

**PUT** `/api/news-groups/{id}`

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| id | Int | 뉴스 그룹 ID |

### Request Body

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| title | String | O | 그룹 제목 (최대 200자) |
| description | String | X | 그룹 설명 |

```json
{
  "title": "AI/반도체 뉴스",
  "description": "AI와 반도체 관련 뉴스 모음"
}
```

### Response (200 OK)

```json
{
  "id": 1,
  "title": "AI/반도체 뉴스",
  "description": "AI와 반도체 관련 뉴스 모음",
  "newsCount": 5,
  "createdAt": "2026-02-20T15:00:00",
  "updatedAt": "2026-02-20T16:00:00"
}
```

---

## 5. 뉴스 그룹 삭제

**DELETE** `/api/news-groups/{id}`

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| id | Int | 뉴스 그룹 ID |

### Response (204 No Content)

---

## 6. 그룹에 뉴스 추가

**POST** `/api/news-groups/{groupId}/news`

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| groupId | Int | 뉴스 그룹 ID |

### Request Body

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| newsIds | List\<Int\> | O | 추가할 뉴스 ID 목록 |

```json
{
  "newsIds": [10, 11, 12]
}
```

### Response (200 OK)

---

## 7. 그룹에서 뉴스 제거

**DELETE** `/api/news-groups/{groupId}/news/{newsId}`

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| groupId | Int | 뉴스 그룹 ID |
| newsId | Int | 제거할 뉴스 ID |

### Response (204 No Content)

---

## 즐겨찾기 연동

뉴스 그룹을 즐겨찾기에 추가/제거할 때는 기존 Favorite API를 사용합니다.

- `targetType`: `NEWS_GROUP`
- `targetId`: 뉴스 그룹 ID
