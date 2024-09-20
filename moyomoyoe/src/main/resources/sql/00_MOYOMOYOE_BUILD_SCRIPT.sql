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
    image_id INT AUTO_INCREMENT COMMENT '이미지번호',
    image_name VARCHAR(20) COMMENT '이미지이름',

    CONSTRAINT pk_image_id PRIMARY KEY (image_id)
) ENGINE=INNODB COMMENT '이미지';

CREATE TABLE IF NOT EXISTS tbl_keyword_id
(
    keyword_id INT AUTO_INCREMENT COMMENT '취미',
    keyword_name VARCHAR(20) NOT NULL COMMENT '취미명',

    CONSTRAINT pk_keyword_id PRIMARY KEY (keyword_id)
) ENGINE=INNODB COMMENT '취미';

CREATE TABLE IF NOT EXISTS tbl_region
(
    region_code INT AUTO_INCREMENT COMMENT '지역코드',
    city VARCHAR(10) NOT NULL COMMENT '시',
    district VARCHAR(10) NOT NULL COMMENT '구',

    CONSTRAINT pk_key_id PRIMARY KEY (region_code)
) ENGINE=INNODB COMMENT '지역';

CREATE TABLE IF NOT EXISTS tbl_user
(
    id INT AUTO_INCREMENT COMMENT '사용자번호',
    username VARCHAR(10) NOT NULL COMMENT '이름',
    account VARCHAR(10) NOT NULL COMMENT '아이디',
    password VARCHAR(20) NOT NULL COMMENT '비밀번호',
    nickname VARCHAR(10) NOT NULL COMMENT '닉네임',
    email VARCHAR(25) NOT NULL COMMENT '이메일',
    phone VARCHAR(20) NOT NULL COMMENT '핸드폰',
    user_role VARCHAR(10) NOT NULL COMMENT '사용자권한',
    image_id INT COMMENT '이미지번호',

    CONSTRAINT pk_id PRIMARY KEY (id),
    CONSTRAINT img_id FOREIGN KEY (image_id) REFERENCES tbl_image (image_id)

) ENGINE=INNODB COMMENT '유저';

CREATE TABLE IF NOT EXISTS tbl_post_list
(
    post_id INT AUTO_INCREMENT COMMENT '게시글번호',
    title VARCHAR(20) NOT NULL COMMENT '제목',
    context VARCHAR(500) NOT NULL COMMENT '내용',
    nickname VARCHAR(20) NOT NULL COMMENT '작성자',
    post_date VARCHAR(20) NOT NULL COMMENT '작성일자',
    user_open BOOLEAN NOT NULL DEFAULT TRUE COMMENT '비회원열람',
    region_code INT NOT NULL COMMENT '지역아이디',
    image_id INT COMMENT '이미지번호',
    keyword_id INT NOT NULL COMMENT '취미',
    user_id INT NOT NULL COMMENT '사용자번호',

    CONSTRAINT pk_post_id PRIMARY KEY (post_id),
    CONSTRAINT fk_region_code FOREIGN KEY (region_code) REFERENCES tbl_region (region_code),
    CONSTRAINT fk_image_id FOREIGN KEY (image_id) REFERENCES tbl_image (image_id),
    CONSTRAINT fk_keyword_id FOREIGN KEY (keyword_id) REFERENCES tbl_keyword_id (keyword_id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES tbl_user (id)
) ENGINE=INNODB COMMENT '게시글목록';

CREATE TABLE IF NOT EXISTS tbl_comment
(
    comment_id INT AUTO_INCREMENT NOT NULL COMMENT '댓글번호',
    post_id INT NOT NULL COMMENT '게시글번호',
    nickname VARCHAR(20) NOT NULL COMMENT '댓글작성자닉네임',
    comment VARCHAR(100) NOT NULL COMMENT '댓글내용',
    comment_post_date VARCHAR(20) NOT NULL COMMENT '댓글작성일자',
    user_id INT NOT NULL COMMENT '사용자번호',

    CONSTRAINT pk_comment_id PRIMARY KEY (comment_id),
    CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES tbl_post_list (post_id),
    CONSTRAINT fk_comment_user_id FOREIGN KEY (user_id) REFERENCES tbl_user (id)
) ENGINE=INNODB COMMENT '댓글';

CREATE TABLE IF NOT EXISTS tbl_store
(
    store_id INT AUTO_INCREMENT COMMENT '사업장아이디',
    store_name VARCHAR(30) NOT NULL COMMENT '사업장이름',
    store_address VARCHAR(30) NOT NULL COMMENT '사업장주소',
    store_sort VARCHAR(30) NOT NULL COMMENT '업종',
    description VARCHAR(500) COMMENT '사업장설명',
    busy_no VARCHAR(10) COMMENT '사업자등록번호',
    id INT NOT NULL COMMENT '사용자번호',
    image_id_store INT COMMENT '이미지번호',

    CONSTRAINT pk_store_id PRIMARY KEY (store_id),
    CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES tbl_user (id),
    CONSTRAINT fk_image_id_store FOREIGN KEY (image_id_store) REFERENCES tbl_image (image_id)
) ENGINE=INNODB COMMENT '사업장';

CREATE TABLE IF NOT EXISTS tbl_schedule
(
    schedule_id INT AUTO_INCREMENT NOT NULL COMMENT '일정번호',
    store_id INT NOT NULL COMMENT '사업장아이디',
    start_time TIME NOT NULL COMMENT '일정시작시간',
    end_time TIME NOT NULL COMMENT '일정종료시간',
    capacity INT NOT NULL COMMENT '수용인원',

    CONSTRAINT pk_schedule_id PRIMARY KEY (schedule_id),
    CONSTRAINT fk_store_id FOREIGN KEY (store_id) REFERENCES tbl_store (store_id)
) ENGINE=INNODB COMMENT '일정';

CREATE TABLE IF NOT EXISTS tbl_reservation
(
    res_id INT AUTO_INCREMENT COMMENT '예약번호',
    user_id_res INT NOT NULL COMMENT '사용자번호',
    res_date DATE NOT NULL COMMENT '날짜',
    customer_num INT NOT NULL COMMENT '인원',
    schedule_id INT NOT NULL COMMENT '일정번호',


    CONSTRAINT pk_res_id PRIMARY KEY (res_id),
    CONSTRAINT fk_user_id_res FOREIGN KEY (user_id_res) REFERENCES tbl_user (id),
    CONSTRAINT fk_schedule_id FOREIGN KEY (schedule_id) REFERENCES tbl_schedule (schedule_id)
) ENGINE=INNODB COMMENT '예약';

INSERT INTO tbl_image (image_name) VALUES ('image1.jpg');

INSERT INTO tbl_keyword_id (keyword_name) VALUES ('영화'),
												 ('독서'),
												 ('스터디'),
												 ('스포츠'),
												 ('여행'),
												 ('기타');

INSERT INTO tbl_region (city, district) VALUES ('서울', '강남구'),
										       ('서울', '강동구'),
										       ('서울', '강북구'),
										       ('서울', '강서구'),
										       ('서울', '관악구'),
										       ('서울', '구로구'),
										       ('서울', '금천구'),
										       ('서울', '노원구'),
										       ('서울', '도봉구'),
										       ('서울', '동대문구'),
										       ('서울', '동작구'),
										       ('서울', '마포구'),
										       ('서울', '서대문구'),
										       ('서울', '서초구'),
										       ('서울', '성동구'),
										       ('서울', '성북구'),
										       ('서울', '송파구'),
										       ('서울', '양천구'),
										       ('서울', '영등포구'),
										       ('서울', '용산구'),
										       ('서울', '은평구'),
										       ('서울', '종로구'),
										       ('서울', '중구'),
										       ('서울', '중랑구');

INSERT INTO tbl_user (username, account, password, nickname, email, phone, user_role, image_id) VALUES ('user1', 'account1', 'password1', 'nick1', 'user1@example.com', '010-1234-5678', 'USER', 1),
																							           ('user2', 'account2', 'password2', 'nick2', 'user2@example.com', '010-2345-6789', 'BUSINESS', NULL),
																						  	           ('user3', 'account3', 'password3', 'nick3', 'user3@example.com', '010-3456-7890', 'USER', NULL),
																							           ('user4', 'account4', 'password4', 'nick4', 'user4@example.com', '010-4567-8901', 'BUSINESS', NULL),
																							           ('admin', 'admin', 'admin', 'admin', 'admin@example.com', '010-5678-9012', 'ADMIN', NULL);

 INSERT INTO tbl_post_list (title, context, nickname, post_date, user_open, region_code, image_id, keyword_id, user_id) VALUES ('첫 번째 게시물', '이것은 첫 번째 게시물의 내용입니다. 서울의 멋진 장소에 대해 이야기합니다.', 'nick1', '2024-09-01', TRUE, 1, NULL, 5, 1),
																														       ('두 번째 게시물', '두 번째 게시물의 내용입니다. 여행에 대한 경험을 나눕니다.', 'nick3', '2024-09-02', TRUE, 2, NULL, 5, 3),
																														       ('세 번째 게시물', '세 번째 게시물은 여름 휴가에 대한 것입니다.', 'nick3', '2024-09-03', FALSE, 3, NULL, 5, 3),
																														       ('네 번째 게시물', '네 번째 게시물은 음식 리뷰에 관한 것입니다.', 'nick1', '2024-09-04', TRUE, 1, NULL, 6, 1),
                                                                                                                               ('다섯 번째 게시글', '영화에 대한 의견을 나눕니다.', '작성자1', '2024-09-20 10:00:00', TRUE, 1, NULL, 1, 1),
                                                                                                                               ('여섯 번째 게시글', '최근 읽은 책에 대한 리뷰입니다.', '작성자2', '2024-09-21 11:30:00', TRUE, 2, NULL, 2, 1),
                                                                                                                               ('일곱 번째 게시글', '최근 스포츠 경기에 대해 이야기해요.', '작성자3', '2024-09-22 12:15:00', FALSE, 1, NULL, 4, 3);

 INSERT INTO tbl_comment (post_id, nickname, comment, comment_post_date, user_id) VALUES (1, 'nick3', '첫 번째 게시물 정말 재미있어요!', '2024-09-06', 1),
																						 (1, 'nick3', '좋은 정보 감사합니다!', '2024-09-07', 1),
																						 (3, 'nick1', '세 번째 게시물 잘 읽었습니다.', '2024-09-09', 3),
																						 (4, 'nick3', '음식 리뷰 기대돼요!', '2024-09-10', 1);

 INSERT INTO tbl_store (store_name, store_address, store_sort, id, description, busy_no, image_id_store) VALUES ('커피합니다', '서울 강남구 테헤란로 123', '카페', 4, '아늑한 분위기의 커피 전문점입니다.', '1234567890', NULL),
																					  	                  ('밥줘', '서울 마포구 홍익로 456', '레스토랑', 2, '신선한 재료로 만든 맛있는 음식을 제공합니다.', '2233445566', NULL);

INSERT INTO tbl_schedule (store_id, start_time, end_time, capacity) VALUES (2, '15:00', '16:00', 30),
																		   (2, '11:00', '13:00', 30),
																		   (1, '10:00', '12:00', 50),
																		   (1, '14:00', '16:00', 50);


INSERT INTO tbl_reservation (user_id_res, res_date, customer_num, schedule_id) VALUES (1, '2024-09-20', 2, 1),
                                                                             (3, '2024-09-22', 4, 2),
                                                                             (1, '2024-09-24', 3, 2),
                                                                             (3, '2024-09-27', 2, 1);






















