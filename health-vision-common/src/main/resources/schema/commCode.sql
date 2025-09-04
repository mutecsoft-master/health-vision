CREATE TABLE IF NOT EXISTS tb_comm_code (
	code_id bigint AUTO_INCREMENT PRIMARY key comment 'ID',
	code_group varchar(100) not null comment '코드그룹',
	code_group_nm varchar(100) comment '코드그룹명',
	code varchar(100) not null comment '코드',
	code_nm varchar(100) comment '코드명',
	sort_order int comment '정렬순서',
	use_yn char(1) not null default 'Y' comment '사용여부',
	reg_id bigint comment '등록자',
	reg_dt datetime comment '등록일시',
	upd_id bigint comment '수정자',
	upd_dt datetime comment '수정일시'
);