-- 기존 외래 키 제약 조건 삭제
ALTER TABLE tbl_comment
DROP FOREIGN KEY fk_post_id;

-- ON DELETE CASCADE를 적용한 새로운 외래 키 제약 조건 추가
ALTER TABLE tbl_comment
ADD CONSTRAINT fk_post_id
FOREIGN KEY (post_id) REFERENCES tbl_post_list(post_id)
ON DELETE CASCADE;