/* 테이블 삭제 */
DROP TABLE IF EXISTS tbl_reservation CASCADE;
DROP TABLE IF EXISTS tbl_schedule CASCADE;
DROP TABLE IF EXISTS tbl_store CASCADE;
DROP TABLE IF EXISTS tbl_business_user CASCADE;
DROP TABLE IF EXISTS tbl_comment CASCADE;
DROP TABLE IF EXISTS tbl_post_list CASCADE;
DROP TABLE IF EXISTS tbl_user CASCADE;
DROP TABLE IF EXISTS tbl_keyword_id CASCADE;
DROP TABLE IF EXISTS tbl_region CASCADE;
DROP TABLE IF EXISTS tbl_image CASCADE;

/* 테이블 생성 */
CREATE TABLE IF NOT EXISTS tbl_image
(
    image_id   INT AUTO_INCREMENT COMMENT '이미지번호',
    image_name VARCHAR(20) COMMENT '이미지이름',
    CONSTRAINT pk_image_id PRIMARY KEY (image_id)
) ENGINE = INNODB COMMENT '이미지';

CREATE TABLE IF NOT EXISTS tbl_keyword_id
(
    keyword_id   INT AUTO_INCREMENT COMMENT '취미',
    keyword_name VARCHAR(20) NOT NULL COMMENT '취미명',
    CONSTRAINT pk_keyword_id PRIMARY KEY (keyword_id)
) ENGINE = INNODB COMMENT '취미';

CREATE TABLE IF NOT EXISTS tbl_region
(
    region_code INT AUTO_INCREMENT COMMENT '지역코드',
    city        VARCHAR(10) NOT NULL COMMENT '시',
    district    VARCHAR(10) NOT NULL COMMENT '구',
    CONSTRAINT pk_key_id PRIMARY KEY (region_code)
) ENGINE = INNODB COMMENT '지역';

CREATE TABLE IF NOT EXISTS tbl_user
(
    id        INT AUTO_INCREMENT COMMENT '사용자번호',
    username  VARCHAR(10) NOT NULL COMMENT '이름',
    account   VARCHAR(10) NOT NULL COMMENT '아이디',
    password  VARCHAR(20) NOT NULL COMMENT '비밀번호',
    nickname  VARCHAR(10) NOT NULL COMMENT '닉네임',
    email     VARCHAR(25) NOT NULL COMMENT '이메일',
    phone     VARCHAR(20) NOT NULL COMMENT '핸드폰',
    user_role VARCHAR(10) NOT NULL COMMENT '사용자권한',
    image_id  INT COMMENT '이미지번호',
    CONSTRAINT pk_id PRIMARY KEY (id),
    CONSTRAINT img_id FOREIGN KEY (image_id) REFERENCES tbl_image (image_id)
) ENGINE = INNODB COMMENT '유저';

CREATE TABLE IF NOT EXISTS tbl_post_list
(
    post_id     INT AUTO_INCREMENT COMMENT '게시글번호',
    title       VARCHAR(20)  NOT NULL COMMENT '제목',
    context     VARCHAR(500) NOT NULL COMMENT '내용',
    nickname    VARCHAR(20)  NOT NULL COMMENT '작성자',
    post_date   VARCHAR(20)  NOT NULL COMMENT '작성일자',
    user_open   BOOLEAN      NOT NULL DEFAULT TRUE COMMENT '비회원열람',
    region_code INT          NOT NULL COMMENT '지역아이디',
    image_id    INT COMMENT '이미지번호',
    keyword_id  INT          NOT NULL COMMENT '취미',
    user_id     INT          NOT NULL COMMENT '사용자번호',
    CONSTRAINT pk_post_id PRIMARY KEY (post_id),
    CONSTRAINT fk_region_code FOREIGN KEY (region_code) REFERENCES tbl_region (region_code),
    CONSTRAINT fk_image_id FOREIGN KEY (image_id) REFERENCES tbl_image (image_id),
    CONSTRAINT fk_keyword_id FOREIGN KEY (keyword_id) REFERENCES tbl_keyword_id (keyword_id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES tbl_user (id)
) ENGINE = INNODB COMMENT '게시글목록';

CREATE TABLE IF NOT EXISTS tbl_comment
(
    comment_id        INT AUTO_INCREMENT NOT NULL COMMENT '댓글번호',
    post_id           INT                NOT NULL COMMENT '게시글번호',
    nickname          VARCHAR(20)        NOT NULL COMMENT '댓글작성자닉네임',
    comment           VARCHAR(100)       NOT NULL COMMENT '댓글내용',
    comment_post_date VARCHAR(20)        NOT NULL COMMENT '댓글작성일자',
    user_id           INT                NOT NULL COMMENT '사용자번호',
    CONSTRAINT pk_comment_id PRIMARY KEY (comment_id),
    CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES tbl_post_list (post_id),
    CONSTRAINT fk_comment_user_id FOREIGN KEY (user_id) REFERENCES tbl_user (id)
) ENGINE = INNODB COMMENT '댓글';

CREATE TABLE IF NOT EXISTS tbl_store
(
    store_id       INT AUTO_INCREMENT COMMENT '사업장아이디',
    store_name     VARCHAR(30) NOT NULL COMMENT '사업장이름',
    store_address  VARCHAR(30) NOT NULL COMMENT '사업장주소',
    store_sort     VARCHAR(30) NOT NULL COMMENT '업종',
    description    VARCHAR(500) COMMENT '사업장설명',
    busy_no        VARCHAR(20) COMMENT '사업자등록번호',
    id             INT         NOT NULL COMMENT '사용자번호',
    image_id_store INT COMMENT '이미지번호',
    CONSTRAINT pk_store_id PRIMARY KEY (store_id),
    CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES tbl_user (id),
    CONSTRAINT fk_image_id_store FOREIGN KEY (image_id_store) REFERENCES tbl_image (image_id)
) ENGINE = INNODB COMMENT '사업장';

CREATE TABLE IF NOT EXISTS tbl_schedule
(
    schedule_id INT AUTO_INCREMENT NOT NULL COMMENT '일정번호',
    store_id    INT                NOT NULL COMMENT '사업장아이디',
    store_name  VARCHAR(100)       NOT NULL COMMENT '사업장명',
    res_date    DATE               NOT NULL COMMENT '예약 날짜',
    start_time  TIME               NOT NULL COMMENT '일정시작시간',
    end_time    TIME               NOT NULL COMMENT '일정종료시간',
    capacity    VARCHAR(10)        NOT NULL COMMENT '수용인원',
    CONSTRAINT pk_schedule_id PRIMARY KEY (schedule_id),
    CONSTRAINT fk_store_id FOREIGN KEY (store_id) REFERENCES tbl_store (store_id)
) ENGINE = INNODB COMMENT '일정';

CREATE TABLE IF NOT EXISTS tbl_reservation
(
    res_id      INT AUTO_INCREMENT COMMENT '예약번호',
    user_id_res INT         NOT NULL COMMENT '사용자번호',
    res_date    DATE        NOT NULL COMMENT '날짜',
    capacity    VARCHAR(10) NOT NULL COMMENT '인원',
    schedule_id INT         NOT NULL COMMENT '일정번호',
    CONSTRAINT pk_res_id PRIMARY KEY (res_id),
    CONSTRAINT fk_user_id_res FOREIGN KEY (user_id_res) REFERENCES tbl_user (id),
    CONSTRAINT fk_schedule_id FOREIGN KEY (schedule_id) REFERENCES tbl_schedule (schedule_id)
) ENGINE = INNODB COMMENT '예약';

/* 더미 데이터 생성 */
INSERT INTO tbl_image (image_name)
VALUES ('image1.jpg');

INSERT INTO tbl_keyword_id (keyword_name)
VALUES ('영화'),
       ('독서'),
       ('스터디'),
       ('스포츠'),
       ('여행'),
       ('기타');

INSERT INTO tbl_region (city, district)
VALUES ('서울', '강남구'),
       ('서울', '강동구'),
       ('서울', '강북구'),
       ('서울', '강서구'),
       ('서울', '관악구'),
       ('서울', '구로구'),
       ('서울', '금천구');

INSERT INTO tbl_user (username, account, password, nickname, email, phone, user_role, image_id)
VALUES ('user1', 'account1', 'password1', 'nick1', 'user1@example.com', '010-1234-5678', 'USER', 1),
       ('user2', 'account2', 'password2', 'nick2', 'user2@example.com', '010-2345-6789', 'BUSINESS', NULL);

-- tbl_store 더미 데이터
INSERT INTO tbl_store (store_id, store_name, store_address, store_sort, description, busy_no, id, image_id_store)
VALUES (1, '스타벅스 강남역점', '서울시 강남구 123', '카페', '전통과 현대가 어우러진 커피전문점', '123-45-6789', 1, NULL),
       (2, '투썸플레이스 명동점', '서울시 중구 456', '카페', '다양한 디저트와 커피를 제공하는 곳', '234-56-7890', 2, NULL),
       (3, '파리바게트 홍대점', '서울시 마포구 789', '베이커리', '신선한 베이커리 제품과 음료 제공', '345-67-8901', 3, NULL),
       (4, '롯데리아 잠실점', '서울시 송파구 101', '패스트푸드', '한국 대표 패스트푸드 체인점', '456-78-9012', 4, NULL),
       (5, '맥도날드 신림점', '서울시 관악구 202', '패스트푸드', '글로벌 패스트푸드 체인', '567-89-0123', 5, 5),
       (6, '서브웨이 서울대점', '서울시 관악구 303', '샌드위치', '신선한 샌드위치를 제공하는 곳', '678-90-1234', 6, NULL),
       (7, '할리스커피 종로점', '서울시 종로구 404', '카페', '프리미엄 커피와 음료를 제공', '789-01-2345', 7, 7),
       (8, '배스킨라빈스 잠실점', '서울시 송파구 505', '아이스크림', '다양한 맛의 아이스크림 제공', '890-12-3456', 8, NULL),
       (9, '카페베네 여의도점', '서울시 영등포구 606', '카페', '한국 대표 카페 프랜차이즈', '901-23-4567', 9, 9),
       (10, '크리스피크림 도넛 강남점', '서울시 강남구 707', '도넛', '달콤한 도넛과 음료 제공', '012-34-5678', 10, NULL);

-- 일정 더미 데이터
INSERT INTO tbl_schedule (store_id, store_name, res_date, start_time, end_time, capacity)
VALUES (1, 'Coffee Bean & Tea Leaf', '2024-09-25', '09:00:00', '10:00:00', '6'),
       (2, '스타벅스 강남역점', '2024-09-25', '10:00:00', '11:00:00', '6'),
       (3, '파리바게트 신촌점', '2024-09-26', '11:00:00', '12:00:00', '6'),
       (4, '롯데리아 홍대점', '2024-09-26', '12:00:00', '13:00:00', '6'),
       (5, '투썸플레이스 서울역점', '2024-09-27', '13:00:00', '14:00:00', '6'),
       (6, '배스킨라빈스 명동점', '2024-09-27', '14:00:00', '15:00:00', '6'),
       (7, '서브웨이 서울대입구점', '2024-09-28', '15:00:00', '16:00:00', '6'),
       (8, '버거킹 신림역점', '2024-09-28', '16:00:00', '17:00:00', '6'),
       (9, '할리스커피 동대문점', '2024-09-29', '17:00:00', '18:00:00', '6'),
       (10, '맥도날드 이태원점', '2024-09-29', '18:00:00', '19:00:00', '6');

-- 예약 더미 데이터
INSERT INTO tbl_reservation (user_id_res, res_date, capacity, schedule_id)
VALUES (1, '2024-09-25', '4', 1),
       (2, '2024-09-25', '2', 2),
       (3, '2024-09-26', '3', 3),
       (4, '2024-09-26', '5', 4),
       (5, '2024-09-27', '6', 5),
       (6, '2024-09-27', '1', 6),
       (7, '2024-09-28', '2', 7),
       (8, '2024-09-28', '3', 8),
       (9, '2024-09-29', '4', 9),
       (10, '2024-09-29', '5', 10);