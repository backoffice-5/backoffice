![header](https://capsule-render.vercel.app/api?type=waving&color=auto&height=200&section=header&text=나나모&fontSize=50)

## 지리고5지조 - 나나모
🖥️ Spring_6기 A반 5조 익명 커뮤니티 나나모(나는 나를 모른다) 🖥️

## ⚙️ 개발 환경
- `Java 8`
- `JDK 17.0.7`
- `MySQL Server 8.0`
- `JPA`

## 🔧 사용한 Tool
<div style="display: flex; justify-content: center;">
  <img src="https://img.shields.io/badge/Java-007396?&style=flat&logo=Java&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=HTML5&logoColor=white" style="margin-right: 10px;"/>
	<img src="https://img.shields.io/badge/CSS3-1572B6?style=flat&logo=CSS3&logoColor=white" style="margin-right: 10px;" />
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=javascript&logoColor=white" />
</div>

<div style="display: flex; justify-content: center;">
  <img src="https://img.shields.io/badge/Spring-6DB33F?&style=flat&logo=spring&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white" style="margin-right: 10px;"/>
  <img src="https://img.shields.io/badge/ApachetTomcat-F8DC75?style=flat&logo=apachetomcat&logoColor=white"/>
</div>

<div style="display: flex; justify-content: center;">
  <img src="https://img.shields.io/badge/Git-F05032?style=flat&logo=git&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/Github-181717?style=flat&logo=github&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/Intellijidea-000000?style=flat&logo=intellijidea&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=postman&logoColor=white" style="margin-right: 10px;">
</div>

## 📍 프로그램 기능

### 1) 사용자 인증 기능
- 회원가입 기능
  - username, password를 Client에서 전달받기
  - username은 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성
  - password는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(A~Z, a~z), 숫자(0~9), 특수문자로 구성
  - 회원 권한 부여하기 (USER, ADMIN)
- 로그인 및 로그아웃 기능
  - username, password를 Client에서 전달받기
  - DB에서 username을 사용해 저장된 회원의 유무를 확인하고, 있다면 password 비교
  - JWT 활용
    
### 2) 프로필 관리
- 프로필 수정 기능
   - 이름, 한줄 소개와 같은 기본적인 정보 수정 가능
   - 비밀번호 수정 시, 비밀번호를 한 번 더 입력받는 과정 필요
   - 최근 3번 안에 사용한 비밀번호는 사용할 수 없도록 제한
     
### 3) 게시물 CRUD 기능
- 게시물 작성, 조회, 수정, 삭제 기능
   - 게시물 조회를 제외한 나머지 기능들은 전부 인가(Authorization) 개념이 적용되어야 하며, 이는 JWT와 같은 토큰으로 검증 가능
   - 오로지 본인만 게시글 수정, 삭제 가능
   - 전체 게시글 정보를 조회하는 기능도 필요
 
### 4) 댓글 CRUD 기능
- 댓글 작성, 조회, 수정, 삭제 기능
   - 게시물에 댓글을 달 수 있고, 본인의 댓글 수정 및 삭제 가능
   - 게시물과 마찬가지로 댓글 조회를 제외한 나머지 기능들은 인가(Authorization) 개념 적용

### 📍 추가 구현
- 백오피스
  - 관리자 페이지 만들기
    - 회원 종류를 일반 회원과 관리자 회원으로 분리
    - 회원 관리 또는 게시글 관리 화면 추가
    
- 프론트엔드 만들기
   - 백엔드에서 제공하는 API를 통해 서버와 통신하는 프론트엔드 구현하기

- 좋아요 기능
   - 게시물 및 댓글 좋아요 / 좋아요 취소 기능
      - 사용자가 게시물이나 댓글에 좋아요를 남기거나 취소할 수 있어야 함
      - 본인이 작성한 게시물 및 댓글 좋아요는 남길 수 없음


## 📆 프로젝트 진행 일정
- 첫째 날 : 프로젝트 내용 정리, 역할 분담
- 둘째 날 : 완성된 기능마다 코드 취합하며 테스트
- 셋째 날 : 기본 기능 완료 및 추가 기능 역할 분담
- 넷째 날 : 추가 기능 완성된 부분마다 코드 취합하며 테스트 
- 다섯째 날: 완성된 코드 취합하고 마무리, 발표 준비

## 👩‍💻 팀원 역할
- 김준영: 게시글 CRUD 및 좋아요, 백오피스 전체
- 서지인: 로그인 및 로그아웃
- 오수정: 회원가입, 백오피스
- 이수연: 댓글 CRUD 및 좋아요, 백오피스
- 추현중: 프로필 관리, 비밀번호 수정

## 📜 API 명세서

회원 관련 API

|기능|Method|URL|
|:---:|:---:|:---|
|회원가입|POST|/api/auth/signup|
|로그인|POST|/api/login|
|로그아웃|-|/api/logout|

게시글 관련 API

|기능|Method|URL|
|:---:|:---:|:---|
|메인 페이지|GET|/|
|게시글 작성 및 수정 페이지|GET|/api/post/(id={id})(required=false)|
|게시글 상세 페이지|GET|/api/post/{id}|
|게시글 전체 조회|GET|/api/posts/{method}|
|게시글 작성|POST|/api/post|
|선택 게시글 조회|GET|/api/post/{id}|
|게시글 수정|PUT|/api/post/{id}|
|게시글 삭제|DELETE|/api/post/{id}|
|게시글 좋아요|POST|/api/post/{id}/like|
|게시글 좋아요 취소|PUT|/api/post/{id}/like|

댓글 관련 API

|기능|Method|URL|
|:---:|:---:|:---|
|댓글 전체 조회|GET|/api/post/comments|
|댓글 작성|POST|/api/post/{postid}/comment|
|댓글 수정|PUT|/api/post/{postid}/comment/{commentid}|
|댓글 삭제|DELETE|/api/post/{postid}/comment/{commentid}|
|댓글 좋아요|POST|/api/post/{postid}/comment/{commentid}/like|
|댓글 좋아요 취소|PUT|/api/post/{postid}/comment/{commentid}/like|

프로필 관련 API

|기능|Method|URL|
|:---:|:---:|:---|
|프로필 조회|GET|/api/profile|
|프로필 수정 페이지|GET|/api/edit_profile/{id}|
|비밀번호 수정 페이지|GET|/api/edit_password|
|프로필 수정 페이지(관리자)|GET|/api/edit_profile|
|프로필 조회|GET|/api/profile/{id}|
|프로필 수정|PUT|/api/profile/update/{id}|
|비밀번호 수정|PUT|/api/profile/password/{id}|
|회원 프로필 개별 조회(관리자)|GET|/api/profile/{username}|
|전체 프로필 조회(관리자)|GET|/api/profiles|
|권한 부여(관리자)|PUT|/api/profile/role/{username}|

통계 관련 API

|기능|Method|URL|
|:---:|:---:|:---|
|통계 페이지 조회|GET|/api/graph|
|통계 데이터 조회|GET|/api/getChartData|
|통계 댓글 데이터 조회|GET|/api/getChartDataComment|
|통계 데이터 조회|GET|/api/get-data|


## 📃 ERD
1) 테이블
   
- users (User.java)
  
|컬럼명|데이터타입|기능|
|:---:|:---:|:---|
|user_id (PK)|BIGINT|사용자ID|
|username|VARCHAR|사용자 이름(ID)|
|nickname|VARCHAR|사용자 닉네임|
|password|VARCHAR|사용자 비밀번호|
|role|enum(‘admin’, ‘user’)|사용자 역할|

- post (Post.java)
  
|컬럼명|데이터타입|기능|
|:---:|:---:|:---|
|id (PK)|BIGINT|게시글ID|
|user_id (FK)|BIGINT|사용자ID|
|title|VARCHAR|게시글 제목|
|content|VARCHAR|게시글 내용|
|username|VARCHAR|사용자 이름(ID)|
|nickname|VARCHAR|사용자 닉네임|
|views|VARCHAR|게시글 조회수|
|like_count|INT|게시글 좋아요 수|
|created_at|DATETIME|게시글 작성일|
|modified_at|DATETIME|게시글 수정일|

- comment (Comment.java)
  
|컬럼명|데이터타입|기능|
|:---:|:---:|:---|
|id (PK)|BIGINT|댓글ID|
|username (FK)|BIGINT|사용자ID|
|post_id (FK)|BIGINT|게시글ID|
|commentcontents|VARCHAR|댓글 내용|
|commentlike_count|INT|댓글 좋아요 수|
|created_at|DATETIME|댓글 작성일|
|modified_at|DATETIME|댓글 수정일|

- post_likes (PostLike.java)
  
|컬럼명|데이터타입|기능|
|:---:|:---:|:---|
|id (PK)|BIGINT|게시글좋아요ID|
|user_id (FK)|BIGINT|사용자ID|
|post_id (FK)|BIGINT|게시글ID|
|post_like|BIT|게시글 좋아요 여부|

- commentlikes (Commentlike.java)
  
|컬럼명|데이터타입|기능|
|:---:|:---:|:---|
|id (PK)|BIGINT|댓글좋아요ID|
|user_id (FK)|BIGINT|사용자ID|
|post_id (FK)|BIGINT|게시글ID|
|comment_id (FK)|BIGINT|댓글ID|
|comment_like|BIT|댓글 좋아요 여부|

- 테이블 관계도
<img src="https://github.com/backoffice-5/backoffice/assets/74248726/6463426e-2deb-4962-84a0-d99dde2b0ed7"  width="800"/>


![Footer](https://capsule-render.vercel.app/api?type=waving&color=auto&height=200&section=footer)
