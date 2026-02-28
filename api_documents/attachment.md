# Attachment API (첨부파일)

**Base URL**: `http://localhost:8081/member-api/api/attachments`

---

## API 목록

| Method | Endpoint | Description | Auth | Content-Type |
|--------|----------|-------------|------|-------------|
| POST | `/` | 파일 업로드 | 필요 | multipart/form-data |
| GET | `/` | 내 첨부파일 목록 조회 | 필요 | application/json |
| GET | `/{id}` | 첨부파일 메타데이터 조회 | 필요 | application/json |
| GET | `/{id}/download` | 파일 다운로드 | 필요 | varies |
| DELETE | `/{id}` | 첨부파일 삭제 (소유자만) | 필요 | - |

---

## POST /

> 파일 업로드 (multipart/form-data)

### Request Parameters (form-data)

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| file | file | O | 업로드할 파일 (최대 10MB) |

### Response `201 Created`

| Field | Type | Description |
|-------|------|-------------|
| id | int | 첨부파일 ID |
| originalFileName | string | 원본 파일명 |
| fileSize | long | 파일 크기 (bytes) |
| contentType | string | MIME type (nullable) |
| fileType | string | 파일 타입 분류 |
| createdAt | string | 생성일시 |

### FileType 분류

| Value | Extensions |
|-------|-----------|
| IMAGE | jpg, jpeg, png, gif, bmp, webp, svg, ico, tiff |
| DOCUMENT | pdf, doc, docx, xls, xlsx, ppt, pptx, txt, csv, hwp, hwpx |
| VIDEO | mp4, avi, mov, wmv, flv, mkv, webm |
| AUDIO | mp3, wav, flac, aac, ogg, wma |
| ARCHIVE | zip, rar, 7z, tar, gz |
| OTHER | 그 외 모든 확장자 |

### Example Request

```
POST /member-api/api/attachments
Authorization: Bearer {token}
Content-Type: multipart/form-data

file: (binary)
```

### Example Response

```json
{
  "id": 1,
  "originalFileName": "삼성전자_분석.pdf",
  "fileSize": 1048576,
  "contentType": "application/pdf",
  "fileType": "DOCUMENT",
  "createdAt": "2026-02-28T10:00:00"
}
```

---

## GET /

> 내 첨부파일 목록 조회 (페이징, 로그인한 회원의 파일만 조회)

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | int | X | 0 | 페이지 번호 (0부터 시작) |
| size | int | X | 20 | 페이지 당 항목 수 |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| attachments | array | 첨부파일 목록 |
| page | int | 현재 페이지 |
| size | int | 페이지 크기 |
| totalElements | long | 전체 항목 수 |
| totalPages | int | 전체 페이지 수 |

### Example Request

```
GET /member-api/api/attachments?page=0&size=10
Authorization: Bearer {token}
```

### Example Response

```json
{
  "attachments": [
    {
      "id": 1,
      "originalFileName": "삼성전자_분석.pdf",
      "fileSize": 1048576,
      "contentType": "application/pdf",
      "fileType": "DOCUMENT",
      "createdAt": "2026-02-28T10:00:00"
    },
    {
      "id": 2,
      "originalFileName": "차트_스크린샷.png",
      "fileSize": 524288,
      "contentType": "image/png",
      "fileType": "IMAGE",
      "createdAt": "2026-02-28T09:30:00"
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

> 첨부파일 메타데이터 조회

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | int | O | 첨부파일 ID |

### Response `200 OK`

| Field | Type | Description |
|-------|------|-------------|
| id | int | 첨부파일 ID |
| originalFileName | string | 원본 파일명 |
| fileSize | long | 파일 크기 (bytes) |
| contentType | string | MIME type (nullable) |
| fileType | string | 파일 타입 분류 |
| createdAt | string | 생성일시 |

### Example Request

```
GET /member-api/api/attachments/1
Authorization: Bearer {token}
```

### Example Response

```json
{
  "id": 1,
  "originalFileName": "삼성전자_분석.pdf",
  "fileSize": 1048576,
  "contentType": "application/pdf",
  "fileType": "DOCUMENT",
  "createdAt": "2026-02-28T10:00:00"
}
```

---

## GET /{id}/download

> 파일 다운로드 (바이너리)

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | int | O | 첨부파일 ID |

### Response `200 OK`

- Content-Type: 파일의 MIME type (또는 application/octet-stream)
- Content-Disposition: `attachment; filename*=UTF-8''{encoded_filename}`
- Body: 파일 바이너리 데이터

### Example Request

```
GET /member-api/api/attachments/1/download
Authorization: Bearer {token}
```

---

## DELETE /{id}

> 첨부파일 삭제 (업로드한 본인만 삭제 가능)

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | int | O | 첨부파일 ID |

### Response `204 No Content`

삭제 성공 (본문 없음)

### Error Cases

| Status | Description |
|--------|-------------|
| 400 | 첨부파일을 찾을 수 없음 |
| 400 | 본인이 업로드한 파일이 아님 |

### Example Request

```
DELETE /member-api/api/attachments/1
Authorization: Bearer {token}
```
