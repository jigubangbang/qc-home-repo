# Quest Community Service Repository

이 레포지토리는 Jigubangbang 프로젝트의 퀘스트 챌린지 및 커뮤니티 관련 마이크로서비스를 관리합니다.

## 개요

MSA(Microservice Architecture) 구조에 따라 게이미피케이션 시스템인 도전 챌린지 페이지 기능과 지구방방의 커뮤니티 페이지 기능을 담당하는 서비스입니다. 퀘스트/뱃지 시스템을 통한 사용자 참여 유도와 여행자 간 소통 및 정보 공유를 지원합니다.

-   **Quest Service:** 퀘스트 도전/완료, 뱃지 획득, 레벨 시스템, 랭킹 등 도전 페이지 관련 모든 기능을 담당합니다.
-   **Community Service:** 여행 메이트 모집, 여행 정보 공유방, 자유 게시판 등 커뮤니티 기능을 담당합니다.

## 주요 기술 스택

-   **Backend:** Java 17, Spring Boot 3, Spring Cloud, MyBatis, MySQL, Lombok, Feign Client
File Storage: AWS S3 (이미지 및 파일 업로드)
-   **Notification:** Chat Service와 연동한 실시간 알림 시스템
-   **Infra:** Eureka, Spring Cloud Gateway, Spring Cloud Config

---

## Quest Service

### 주요 기능

-   **게이미피케이션 시스템:** 60개의 퀘스트를 난이도별로 체계화하고 세부 카테고리로 세분화했으며, 33개의 테마별 뱃지 시스템을 구축했습니다.
-   **퀘스트 관리:** 사용자는 다양한 여행 관련 퀘스트에 도전하고 인증을 통해 완료할 수 있습니다. 시즌 한정 퀘스트를 통해 지속적인 재참여를 유도합니다.
-   **뱃지 시스템:** 퀘스트 완료 시 관련 뱃지를 자동으로 획득하며, 대표 뱃지 설정을 통해 프로필에 표시할 수 있습니다.
-   **XP 및 레벨 시스템:** 퀘스트 완료 시 XP를 획득하고 누적 XP에 따라 레벨이 상승합니다. 능동적인 여행 계획과 기록을 유도합니다.
-   **랭킹 시스템:** 주간/전체 퀘스트 완료 수, 레벨 등 다양한 기준으로 사용자 랭킹을 제공합니다.
-   **관리자 기능:** 퀘스트/뱃지 생성, 수정, 삭제 및 사용자 인증 취소 기능을 제공합니다.

### API Endpoints

#### User-Facing REST API

| Method   | URL                                         | Role | 설명                                |
| :------- | :------------------------------------------ | :--- | :---------------------------------- |
| `GET`    | `/api/quests/list`                          | ALL  | 퀘스트 목록 조회 (필터링/검색)       |
| `GET`    | `/api/quests/detail/{quest_id}`             | ALL  | 퀘스트 상세 정보 조회                |
| `GET`    | `/api/quests/badges`                        | ALL  | 뱃지 목록 조회                       |
| `GET`    | `/api/quests/badges/{badge_id}`             | ALL  | 뱃지 상세 정보 조회                  |
| `GET`    | `/api/quests/weekly-quest`                  | ALL  | 주간 퀘스트 랭킹                     |
| `GET`    | `/api/quests/weekly-level`                  | ALL  | 주간 레벨 랭킹                       |
| `GET`    | `/api/quests/top-level`                     | ALL  | 전체 레벨 랭킹                       |
| `GET`    | `/api/quests/top-quest`                     | ALL  | 전체 퀘스트 랭킹                     |
| `GET`    | `/api/quests/rankings`                      | ALL  | 랭킹 목록 조회 (페이징/검색)         |
| `GET`    | `/api/quests/my-page`                       | ALL  | 사용자 페이지 데이터 조회            |
| `GET`    | `/api/quests/user-graph/{user_id}`          | ALL  | 사용자 그래프 데이터 조회            |
| `GET`    | `/api/quests/user-level/{user_id}`          | ALL  | 사용자 레벨 정보 조회                |

#### User Authenticated REST API

| Method   | URL                                             | Role | 설명                                 |
| :------- | :---------------------------------------------- | :--- | :---------------------------------- |
| `GET`    | `/api/user-quests/list`                         | USER | 내 퀘스트 목록 조회                   |
| `GET`    | `/api/user-quests/detail/{quest_id}`            | USER | 퀘스트 상세 조회 (도전 상태 포함)     |
| `POST`   | `/api/user-quests/challenge/{quest_id}`         | USER | 퀘스트 도전 시작                      |
| `POST`   | `/api/user-quests/reChallenge/{quest_id}`       | USER | 퀘스트 재도전                         |
| `POST`   | `/api/user-quests/{quest_user_id}/complete`     | USER | 퀘스트 완료 제출                      |
| `POST`   | `/api/user-quests/{quest_user_id}/abandon`      | USER | 퀘스트 포기                           |
| `POST`   | `/api/user-quests/{quest_user_id}/upload-image` | USER | 퀘스트 인증 이미지 업로드             |
| `GET`    | `/api/user-quests/journey`                      | USER | 내 여행 여정 조회                     |
| `GET`    | `/api/user-quests/detail`                       | USER | 내 퀘스트 진행 현황 조회              |
| `GET`    | `/api/user-quests/badges`                       | USER | 내 뱃지 정보 조회                     |
| `GET`    | `/api/user-quests/badges/my`                    | USER | 획득한 뱃지 목록 조회                 |
| `GET`    | `/api/user-quests/badges/{badge_id}`            | USER | 뱃지 상세 조회 (획득 상태 포함)       |
| `POST`   | `/api/user-quests/badges/{badge_id}/pin`        | USER | 대표 뱃지 설정                        |
| `GET`    | `/api/user-quests/ranking/my`                   | USER | 내 랭킹 정보 조회                     |

#### Admin REST API

| Method   | URL                                                        | Role  | 설명                           |
| :------- | :--------------------------------------------------------- | :---- | :---------------------------- |
| `GET`    | `/api/admin-quests/list`                                   | ADMIN | 관리자 퀘스트 목록 조회         |
| `GET`    | `/api/admin-quests/detail/{quest_id}`                      | ADMIN | 관리자 퀘스트 상세 조회         |
| `GET`    | `/api/admin-quests/detail/{quest_id}/users`               | ADMIN | 퀘스트 참여자 목록 조회         |
| `GET`    | `/api/admin-quests/detail/{quest_id}/badges`              | ADMIN | 퀘스트 연결 뱃지 조회           |
| `POST`   | `/api/admin-quests/quests`                                 | ADMIN | 퀘스트 생성                     |
| `PUT`    | `/api/admin-quests/quests/{quest_id}`                      | ADMIN | 퀘스트 수정                     |
| `DELETE` | `/api/admin-quests/quests/{quest_id}`                      | ADMIN | 퀘스트 삭제                     |
| `PUT`    | `/api/admin-quests/quests-certi/{quest_user_id}/reject`    | ADMIN | 퀘스트 인증 거절               |
| `GET`    | `/api/admin-quests/badges`                                 | ADMIN | 관리자 뱃지 목록 조회           |
| `GET`    | `/api/admin-quests/badges/{badge_id}`                      | ADMIN | 관리자 뱃지 상세 조회           |
| `POST`   | `/api/admin-quests/badges`                                 | ADMIN | 뱃지 생성                       |
| `PUT`    | `/api/admin-quests/badges/{badge_id}`                      | ADMIN | 뱃지 수정                       |
| `DELETE` | `/api/admin-quests/badges/{badge_id}`                      | ADMIN | 뱃지 삭제                       |
| `POST`   | `/api/admin-quests/upload-image/{type}`                    | ADMIN | 관리자 이미지 업로드           |
| `GET`    | `/api/admin-quests/badges/check-id/{badgeId}`              | ADMIN | 뱃지 ID 중복 확인              |

### 데이터베이스 스키마

#### `quest_category`

퀘스트 카테고리를 저장하는 테이블입니다.

| Column     | Type           | Description         |
| :--------- | :------------- | :------------------ |
| `id`       | INT (PK, AI)   | 카테고리 고유 ID     |
| `title`    | VARCHAR(20)    | 카테고리 제목        |
| `subtitle` | VARCHAR(20)    | 카테고리 부제목      |

#### `quest`

퀘스트 정보를 저장하는 테이블입니다.

| Column         | Type           | Description                         |
| :------------- | :------------- | :---------------------------------- |
| `id`           | INT (PK, AI)   | 퀘스트 고유 ID                      |
| `type`         | ENUM           | 퀘스트 타입 (`AUTH`, `CHECK`)       |
| `category`     | INT (FK)       | 퀘스트 카테고리 ID                  |
| `title`        | VARCHAR(100)   | 퀘스트 제목                         |
| `description`  | TEXT           | 퀘스트 설명                         |
| `difficulty`   | ENUM           | 난이도 (`EASY`, `MEDIUM`, `HARD`)   |
| `xp`           | INT            | 완료 시 획득 XP                     |
| `is_seasonal`  | BOOLEAN        | 시즌 퀘스트 여부                    |
| `season_start` | TIMESTAMP      | 시즌 시작일                         |
| `season_end`   | TIMESTAMP      | 시즌 종료일                         |
| `status`       | ENUM           | 퀘스트 상태 (`ACTIVE`, `INACTIVE`)  |
| `created_at`   | TIMESTAMP      | 생성일                              |

#### `quest_user`

사용자별 퀘스트 진행 상태를 저장하는 테이블입니다.

| Column         | Type             | Description                                   |
| :------------- | :--------------- | :-------------------------------------------- |
| `id`           | INT (PK, AI)     | 고유 ID                                        |
| `user_id`      | VARCHAR(20) (FK) | 사용자 ID                                      |
| `quest_id`     | INT (FK)         | 퀘스트 ID                                      |
| `description`  | TEXT             | 인증 설명                                      |
| `status`       | ENUM             | 상태 (`IN_PROGRESS`, `COMPLETED`, `GIVEN_UP`, `PENDING`) |
| `started_at`   | TIMESTAMP        | 시작일                                         |
| `completed_at` | TIMESTAMP        | 완료일                                         |
| `given_up_at`  | TIMESTAMP        | 포기일                                         |

#### `quest_image`

퀘스트 인증 이미지를 저장하는 테이블입니다.

| Column         | Type           | Description           |
| :------------- | :------------- | :-------------------- |
| `id`           | INT (PK, AI)   | 고유 ID                |
| `quest_user_id`| INT (FK)       | 퀘스트 유저 ID         |
| `image`        | VARCHAR(255)   | 이미지 URL             |

#### `badge`

뱃지 정보를 저장하는 테이블입니다.

| Column       | Type           | Description            |
| :----------- | :------------- | :--------------------- |
| `id`         | INT (PK, AI)   | 뱃지 고유 ID            |
| `kor_title`  | VARCHAR(100)   | 한국어 뱃지명           |
| `eng_title`  | VARCHAR(100)   | 영어 뱃지명             |
| `description`| TEXT           | 뱃지 설명               |
| `icon`       | VARCHAR(255)   | 뱃지 아이콘 URL         |
| `difficulty` | INT            | 난이도 (1: Easy, 2: Normal, 3: Hard) |
| `created_at` | TIMESTAMP      | 생성일                  |

#### `badge_user`

사용자별 뱃지 획득 내역을 저장하는 테이블입니다.

| Column        | Type             | Description              |
| :------------ | :--------------- | :----------------------- |
| `id`          | INT (PK, AI)     | 고유 ID                   |
| `user_id`     | VARCHAR(20) (FK) | 사용자 ID                 |
| `badge_id`    | INT (FK)         | 뱃지 ID                   |
| `awarded_at`  | TIMESTAMP        | 획득일                    |
| `is_displayed`| BOOLEAN          | 프로필 표시 여부          |
| `pinned_at`   | TIMESTAMP        | 대표 뱃지 설정일          |

#### `badge_quest`

뱃지와 퀘스트의 연관관계를 저장하는 테이블입니다.

| Column      | Type         | Description     |
| :---------- | :----------- | :-------------- |
| `id`        | INT (PK, AI) | 고유 ID          |
| `badge_id`  | INT (FK)     | 뱃지 ID          |
| `quest_id`  | INT (FK)     | 퀘스트 ID        |

## 실행 방법

1.  **Config Server 실행:** 설정 정보를 가져오기 위해 Config Server를 먼저 실행해야 합니다.
2.  **Eureka Server 실행:** 서비스 디스커버리를 위해 Eureka Server를 실행합니다.
3.  **Quest Service 실행:**
    ```bash
    # quest-service 디렉토리로 이동하여 아래 명령어 실행
    ./mvnw spring-boot:run
    ```
4.  **API Gateway 실행:** 라우팅을 위해 API Gateway를 실행합니다.
5.  **프론트엔드 실행:** `msa-front` 디렉토리에서 WebSocket을 지원하는 클라이언트를 실행합니다.

---

## Community Service 

### 주요 기능
-   **여행 메이트 모집:** 사용자는 여행 동행인을 구하는 모임을 생성하고 참여할 수 있습니다. 다양한 필터(지역, 테마, 여행스타일 등)를 통해 원하는 모임을 검색할 수 있습니다.
-   **여행 정보 공유방:** 특정 여행 주제로 사용자들 간 소통이 이루어지는 공간입니다. 테마별로 분류되어 있으며 즉시 참여가 가능합니다.
-   **자유 게시판:** 여행 관련 자유로운 소통을 위한 게시판입니다. 사진 업로드, 댓글/대댓글, 좋아요/북마크 기능을 지원합니다.
-   **실시간 알림:** 게시글 댓글, 여행 모임 신청/수락 등의 이벤트에 대한 실시간 알림을 제공합니다.
-   **신고 시스템:** 부적절한 콘텐츠나 사용자에 대한 신고 기능을 제공합니다.

### API Endpoints

#### User-Facing REST API

| Method  | URL                                           | Role | 설명                             |
| :------ | :--------------------------------------------| :--- | :------------------------------ |
| `GET`   | `/com/countries`                              | ALL  | 국가 목록 조회                  |
| `GET`   | `/com/cities/{countryId}`                     | ALL  | 도시 목록 조회                  |
| `GET`   | `/com/targets`                                | ALL  | 여행 목적 목록 조회             |
| `GET`   | `/com/themes`                                 | ALL  | 여행 테마 목록 조회             |
| `GET`   | `/com/travel-styles`                          | ALL  | 여행 스타일 목록 조회           |
| `GET`   | `/com/travelmate/list`                        | ALL  | 여행 메이트 목록 조회           |
| `GET`   | `/com/travelmate/{postId}`                    | ALL  | 여행 메이트 상세 조회           |
| `GET`   | `/com/travelmate/{postId}/status`             | ALL  | 여행 메이트 상태 조회           |
| `GET`   | `/com/travelmate/{postId}/members`            | ALL  | 여행 메이트 멤버 목록 조회      |
| `GET`   | `/com/travelmate/{postId}/comments`           | ALL  | 여행 메이트 댓글 조회           |
| `GET`   | `/com/travelinfo/list`                        | ALL  | 여행 정보 공유방 목록 조회      |
| `GET`   | `/com/travelinfo/{id}`                         | ALL  | 여행 정보 공유방 상세 조회      |
| `GET`   | `/com/board/list`                             | ALL  | 게시판 목록 조회                |
| `GET`   | `/com/board/{postId}`                         | ALL  | 게시글 상세 조회 (조회수 증가) |
| `GET`   | `/com/board/{postId}/info`                    | ALL  | 게시글 정보 조회 (조회수 증가 안함) |
| `GET`   | `/com/board/{postId}/comments`                | ALL  | 게시글 댓글 조회               |
| `GET`   | `/com/board/{postId}/images`                  | ALL  | 게시글 이미지 조회             |
| `GET`   | `/com/user-profile/{userId}`                   | ALL  | 사용자 프로필 조회             |
| `POST`  | `/com/chat`                                   | ALL  | 채팅방 정보 조회/생성          |
| `POST`  | `/com/upload-image/{type}`                     | ALL  | 이미지 업로드                  |

### User Authenticated REST API

| Method  | URL                                           | Role | 설명                             |
| :------ | :--------------------------------------------| :--- | :------------------------------ |
| `POST`  | `/user-com/travelmate`                        | USER | 여행 메이트 모임 생성           |
| `PUT`   | `/user-com/travelmate/{postId}`               | USER | 여행 메이트 모임 수정           |
| `DELETE`| `/user-com/travelmate/{travelmateId}`         | USER | 여행 메이트 모임 삭제           |
| `POST`  | `/user-com/travelmate/{postId}/join`          | USER | 여행 메이트 모임 참여 신청      |
| `GET`   | `/user-com/travelmate/{postId}/member-status`| USER | 여행 메이트 멤버 상태 조회      |
| `POST`  | `/user-com/travelmate/{postId}/comments`      | USER | 여행 메이트 댓글 작성           |
| `PUT`   | `/user-com/travelmate/{postId}/comments/{commentId}` | USER | 여행 메이트 댓글 수정           |
| `DELETE`| `/user-com/travelmate/{postId}/comments/{commentId}` | USER | 여행 메이트 댓글 삭제           |
| `POST`  | `/user-com/travelmate/{postId}/comments/{parentId}/replies` | USER | 여행 메이트 답글 작성           |
| `POST`  | `/user-com/travelmate/like/{postId}`           | USER | 여행 메이트 좋아요 추가         |
| `DELETE`| `/user-com/travelmate/like/{postId}`           | USER | 여행 메이트 좋아요 제거         |
| `GET`   | `/user-com/travelmate/likes`                    | USER | 좋아요한 여행 메이트 목록 조회  |
| `POST`  | `/user-com/travelmate/{travelmateId}/application/{applicationId}/{action}` | USER | 여행 메이트 신청 승인/거절      |
| `DELETE`| `/user-com/travelmate/{travelmateId}/application/{applicationId}` | USER | 여행 메이트 신청 삭제           |
| `POST`  | `/user-com/travelinfo`                         | USER | 여행 정보 공유방 생성           |
| `PUT`   | `/user-com/travelinfo/{id}`                    | USER | 여행 정보 공유방 수정           |
| `DELETE`| `/user-com/travelinfo/{travelinfoId}`          | USER | 여행 정보 공유방 삭제           |
| `POST`  | `/user-com/travelinfo/{travelInfoId}/join`     | USER | 여행 정보 공유방 참여           |
| `POST`  | `/user-com/travelinfo/like/{travelInfoId}`     | USER | 여행 정보 공유방 좋아요 추가    |
| `DELETE`| `/user-com/travelinfo/like/{travelInfoId}`     | USER | 여행 정보 공유방 좋아요 제거    |
| `GET`   | `/user-com/travelinfo/likes`                    | USER | 좋아요한 여행 정보 공유방 목록 조회 |
| `GET`   | `/user-com/travelinfo/joined-chats`             | USER | 참여한 여행 정보 공유방 목록 조회 |
| `POST`  | `/user-com/board`                              | USER | 게시글 작성                   |
| `PUT`   | `/user-com/board/{postId}`                      | USER | 게시글 수정                   |
| `DELETE`| `/user-com/board/{postId}`                      | USER | 게시글 삭제                   |
| `POST`  | `/user-com/board/{postId}/comments`             | USER | 댓글 작성                   |
| `PUT`   | `/user-com/board/{postId}/comments/{commentId}` | USER | 댓글 수정                   |
| `DELETE`| `/user-com/board/{postId}/comments/{commentId}` | USER | 댓글 삭제                   |
| `POST`  | `/user-com/board/{postId}/comments/{parentId}/replies` | USER | 대댓글 작성                 |
| `POST`  | `/user-com/board/like/{postId}`                  | USER | 게시글 좋아요 추가           |
| `DELETE`| `/user-com/board/like/{postId}`                  | USER | 게시글 좋아요 제거           |
| `GET`   | `/user-com/board/likes`                           | USER | 좋아요한 게시글 목록 조회    |
| `POST`  | `/user-com/board/bookmark/{postId}`              | USER | 게시글 북마크 추가           |
| `DELETE`| `/user-com/board/bookmark/{postId}`              | USER | 게시글 북마크 제거           |
| `GET`   | `/user-com/board/bookmarks`                       | USER | 북마크한 게시글 목록 조회    |
| `GET`   | `/user-com/board/my-posts`                        | USER | 내가 작성한 게시글 조회      |
| `GET`   | `/user-com/board/my-comments`                     | USER | 내가 댓글 단 게시글 조회     |
| `GET`   | `/user-com/board/my-bookmarks`                    | USER | 내가 북마크한 게시글 조회    |
| `GET`   | `/user-com/board/my-likes`                        | USER | 내가 좋아요한 게시글 조회    |
| `POST`  | `/user-com/report`                              | USER | 신고 접수                   |
| `GET`   | `/user-com/my-traveler`                         | USER | 내 여행자 데이터 조회        |
| `GET`   | `/user-com/style`                               | USER | 내 여행 스타일 조회          |

### 데이터베이스 스키마 

#### `board_category`

게시판 카테고리를 저장하는 테이블입니다.

| Column       | Type           | Description     |
| :----------- | :------------- | :-------------- |
| `id`         | INT (PK, AI)   | 카테고리 고유 ID |
| `title`      | VARCHAR(100)   | 카테고리 제목    |
| `description`| TEXT           | 카테고리 설명    |
| `created_at` | TIMESTAMP      | 생성일           |

#### `board_post`

자유 게시판 게시글을 저장하는 테이블입니다.

| Column           | Type             | Description                             |
| :--------------- | :--------------- | :-------------------------------------- |
| `id`             | INT (PK, AI)     | 게시글 고유 ID                          |
| `board_id`       | INT (FK)         | 게시판 카테고리 ID                      |
| `user_id`        | VARCHAR(20) (FK) | 작성자 ID                               |
| `title`          | VARCHAR(255)     | 게시글 제목                             |
| `content`        | TEXT             | 게시글 내용                             |
| `type`           | ENUM             | 게시글 타입 (`NORMAL`, `NOTICE`, `VOTE`) |
| `icon`           | VARCHAR(50)      | 아이콘                                  |
| `view_count`     | INT              | 조회수                                  |
| `like_count`     | INT              | 좋아요 수                               |
| `bookmark_count` | INT              | 북마크 수                               |
| `created_at`     | TIMESTAMP        | 생성일                                  |
| `updated_at`     | TIMESTAMP        | 수정일                                  |
| `blind_status`   | ENUM             | 블라인드 상태 (`VISIBLE`, `BLINDED`)     |

#### `board_image`

| Column      | Type           | Description                      |
| :---------- | :------------- | :------------------------------- |
| `id`        | `INT (PK, AI)` | 이미지 고유 ID                    |
| `post_id`   | `INT (FK)`     | 게시글 ID (`board_post.id` 참조)  |
| `image_url` | `VARCHAR(255)` | 이미지 URL                        |

#### `board_comment`

게시판 댓글을 저장하는 테이블입니다.

| Column        | Type             | Description                            |
| :------------ | :--------------- | :------------------------------------- |
| `id`          | INT (PK, AI)     | 댓글 고유 ID                           |
| `user_id`     | VARCHAR(20) (FK) | 사용자 ID                              |
| `post_id`     | INT (FK)         | 게시글 ID                              |
| `content`     | TEXT             | 댓글 내용                               |
| `level`       | INT              | 댓글 레벨 (`0`: 댓글, `1`: 대댓글)     |
| `parent_id`   | INT (FK)         | 부모 댓글 ID                           |
| `created_at`  | TIMESTAMP        | 생성일                                 |
| `updated_at`  | TIMESTAMP        | 수정일                                 |
| `blind_status`| ENUM             | 블라인드 상태 (`VISIBLE`, `BLINDED`)    |
| `is_deleted`  | BOOLEAN          | 삭제 여부                               |

#### `board_like`

게시판 좋아요를 저장하는 테이블입니다.

| Column       | Type             | Description     |
| :----------- | :--------------- | :-------------- |
| `id`         | INT (PK, AI)     | 고유 ID         |
| `user_id`    | VARCHAR(20) (FK) | 사용자 ID       |
| `post_id`    | INT (FK)         | 게시글 ID       |
| `created_at` | TIMESTAMP        | 생성일          |

#### `board_bookmark`

게시판 북마크를 저장하는 테이블입니다.

| Column       | Type             | Description     |
| :----------- | :--------------- | :-------------- |
| `id`         | INT (PK, AI)     | 고유 ID         |
| `user_id`    | VARCHAR(20) (FK) | 사용자 ID       |
| `post_id`    | INT (FK)         | 게시글 ID       |
| `created_at` | TIMESTAMP        | 생성일          |

#### `travel_theme`

여행 테마를 저장하는 테이블입니다.

| Column    | Type           | Description          |
| :-------- | :------------- | :------------------- |
| `id`      | INT (PK, AI)   | 테마 고유 ID         |
| `code`    | VARCHAR(50)    | 테마 코드 (중복 불가) |
| `label`   | VARCHAR(255)   | 테마명               |

#### `travelmate_post`

여행 메이트 모집글을 저장하는 테이블입니다.

| Column                  | Type             | Description                                 |
| :---------------------- | :--------------- | :------------------------------------------ |
| `id`                    | INT (PK, AI)     | 모집글 고유 ID                              |
| `creator_id`            | VARCHAR(20) (FK) | 작성자 ID                                    |
| `title`                 | VARCHAR(50)      | 모집글 제목                                 |
| `simple_description`    | VARCHAR(255)     | 간단 설명                                   |
| `description`           | TEXT             | 상세 설명                                   |
| `application_description`| TEXT            | 신청 안내                                   |
| `gender_filter`         | ENUM             | 성별 필터 (`ALL`, `MALE`, `FEMALE`)         |
| `age_filter`            | ENUM             | 연령 필터 (`ALL`, `20`, `30`, ...)          |
| `target_filter`         | ENUM             | 동반 유형 필터 (`ALL`, `ALONE`, ...)        |
| `background_image`      | VARCHAR(255)     | 배경 이미지 URL                             |
| `thumbnail_image`       | VARCHAR(255)     | 썸네일 이미지 URL                           |
| `is_public`             | BOOLEAN          | 공개 여부                                   |
| `view_count`            | INT              | 조회수                                      |
| `status`                | ENUM             | 모집 상태 (`ACTIVE`, `END`, `DELETED`)      |
| `start_at`              | TIMESTAMP        | 여행 시작일                                 |
| `end_at`                | TIMESTAMP        | 여행 종료일                                 |
| `created_at`            | TIMESTAMP        | 생성일                                      |
| `updated_at`            | TIMESTAMP        | 수정일                                      |
| `deleted_at`            | TIMESTAMP        | 삭제일                                      |
| `blind_status`          | ENUM             | 블라인드 상태 (`VISIBLE`, `BLINDED`)        |

#### `travelmate_theme`

여행 메이트 모집글과 테마의 연관관계를 저장하는 테이블입니다.

| Column     | Type         | Description                  |
| :--------- | :----------- | :--------------------------- |
| `id`       | INT (PK, AI) | 고유 ID                      |
| `mate_id`  | INT (FK)     | 여행 메이트 모집글 ID        |
| `theme_id` | INT (FK)     | 여행 테마 ID                 |

#### `travelmate_style`

여행 메이트 모집글과 여행 스타일의 연관관계를 저장하는 테이블입니다.

| Column     | Type           | Description                  |
| :--------- | :------------- | :--------------------------- |
| `id`       | INT (PK, AI)   | 고유 ID                      |
| `mate_id`  | INT (FK)       | 여행 메이트 모집글 ID        |
| `style_id` | CHAR(1) (FK)   | 여행 스타일 ID               |

#### `travelmate_region`

여행 메이트 모집글과 지역의 연관관계를 저장하는 테이블입니다.

| Column      | Type           | Description                  |
| :---------- | :------------- | :--------------------------- |
| `id`        | INT (PK, AI)   | 고유 ID                      |
| `mate_id`   | INT (FK)       | 여행 메이트 모집글 ID        |
| `country_id`| VARCHAR(5) (FK)| 국가 ID                      |
| `city_id`   | INT (FK)       | 도시 ID                      |

#### `travelmate_application`

여행 메이트 신청 정보를 저장하는 테이블입니다.

| Column              | Type             | Description                         |
| :------------------ | :--------------- | :---------------------------------- |
| `id`                | INT (PK, AI)     | 신청 고유 ID                        |
| `user_id`           | VARCHAR(20) (FK) | 신청자 ID                           |
| `mate_id`           | INT (FK)         | 여행 메이트 모집글 ID               |
| `application_comment`| TEXT            | 신청 메시지                         |
| `status`            | ENUM             | 신청 상태 (`PENDING`, `ACCEPTED`, `REJECTED`) |
| `applied_at`        | TIMESTAMP        | 신청일                              |
| `responded_at`      | TIMESTAMP        | 응답일                              |

#### `travelmate_comment`

여행 메이트 댓글을 저장하는 테이블입니다.

| Column        | Type             | Description                           |
| :------------ | :--------------- | :------------------------------------ |
| `id`          | INT (PK, AI)     | 댓글 고유 ID                          |
| `user_id`     | VARCHAR(20) (FK) | 사용자 ID                             |
| `mate_id`     | INT (FK)         | 여행 메이트 모집글 ID                 |
| `content`     | TEXT             | 댓글 내용                              |
| `level`       | INT              | 댓글 레벨 (`0`: 댓글, `1`: 대댓글)     |
| `parent_id`   | INT (FK)         | 부모 댓글 ID                          |
| `created_at`  | TIMESTAMP        | 생성일                                |
| `updated_at`  | TIMESTAMP        | 수정일                                |
| `blind_status`| ENUM             | 블라인드 상태 (`VISIBLE`, `BLINDED`)   |
| `is_deleted`  | BOOLEAN          | 삭제 여부                              |

#### `travelmate_like`

여행 메이트 좋아요를 저장하는 테이블입니다.

| Column       | Type             | Description               |
| :----------- | :--------------- | :------------------------ |
| `id`         | INT (PK, AI)     | 고유 ID                   |
| `user_id`    | VARCHAR(20) (FK) | 사용자 ID                 |
| `mate_id`    | INT (FK)         | 여행 메이트 모집글 ID     |
| `created_at` | TIMESTAMP        | 생성일                    |

#### `info_theme`

정보 공유방 테마를 저장하는 테이블입니다.

| Column    | Type           | Description          |
| :-------- | :------------- | :------------------- |
| `id`      | INT (PK, AI)   | 테마 고유 ID         |
| `code`    | VARCHAR(50)    | 테마 코드 (중복 불가) |
| `label`   | VARCHAR(255)   | 테마명               |

#### `travel_info`

여행 정보 공유방을 저장하는 테이블입니다.

| Column              | Type             | Description                         |
| :------------------ | :--------------- | :---------------------------------- |
| `id`                | INT (PK, AI)     | 공유방 고유 ID                      |
| `creator_id`        | VARCHAR(20) (FK) | 작성자 ID                           |
| `title`             | VARCHAR(50)      | 공유방 제목                         |
| `simple_description`| TEXT             | 간단 설명                           |
| `enter_description` | TEXT             | 입장 안내                           |
| `thumbnail_image`   | VARCHAR(255)     | 썸네일 이미지 URL                   |
| `created_at`        | TIMESTAMP        | 생성일                              |
| `deleted_at`        | TIMESTAMP        | 삭제일                              |
| `blind_status`      | ENUM             | 블라인드 상태 (`VISIBLE`, `BLINDED`) |

#### `travel_info_theme`

여행 정보 공유방과 테마의 연관관계를 저장하는 테이블입니다.

| Column           | Type         | Description             |
| :--------------- | :----------- | :---------------------- |
| `id`             | INT (PK, AI) | 고유 ID                 |
| `travel_info_id` | INT (FK)     | 여행 정보 공유방 ID     |
| `theme_id`       | INT (FK)     | 정보 테마 ID            |

#### `travel_info_like`

여행 정보 공유방 좋아요를 저장하는 테이블입니다.

| Column           | Type             | Description             |
| :--------------- | :--------------- | :---------------------- |
| `id`             | INT (PK, AI)     | 고유 ID                 |
| `user_id`        | VARCHAR(20) (FK) | 사용자 ID               |
| `travel_info_id` | INT (FK)         | 여행 정보 공유방 ID     |
| `created_at`     | TIMESTAMP        | 생성일                  |

#### `group_member`

그룹 활동 회원을 저장하는 테이블입니다.

| Column       | Type             | Description                             |
| :----------- | :--------------- | :-------------------------------------- |
| `id`         | INT (PK, AI)     | 고유 ID                                 |
| `group_type` | ENUM             | 그룹 타입 (`TRAVELMATE`, `TRAVELINFO`)   |
| `group_id`   | INT              | 그룹 ID                                 |
| `user_id`    | VARCHAR(20) (FK) | 사용자 ID                               |
| `joined_at`  | TIMESTAMP        | 참여일                                  |
| `is_creator` | BOOLEAN          | 생성자 여부                             |

## 실행 방법

1.  **Config Server 실행:** 설정 정보를 가져오기 위해 Config Server를 먼저 실행해야 합니다.
2.  **Eureka Server 실행:** 서비스 디스커버리를 위해 Eureka Server를 실행합니다.
3.  **Chat Service 실행:**
    ```bash
    # chat-service 디렉토리로 이동하여 아래 명령어 실행
    ./mvnw spring-boot:run
    ```
4.  **API Gateway 실행:** 라우팅을 위해 API Gateway를 실행합니다.
5.  **프론트엔드 실행:** `msa-front` 디렉토리에서 WebSocket을 지원하는 클라이언트를 실행합니다.