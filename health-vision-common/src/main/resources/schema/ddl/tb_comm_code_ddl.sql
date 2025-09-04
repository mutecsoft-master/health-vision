-- auto-generated definition
create table tb_comm_code
(
    code_id       bigint auto_increment comment 'ID'
        primary key,
    code_group    varchar(100)     not null comment '코드그룹',
    code_group_nm varchar(100)     null comment '코드그룹명',
    code          varchar(100)     not null comment '코드',
    code_nm       varchar(100)     null comment '코드명',
    sort_order    int              null comment '정렬순서',
    use_yn        char default 'Y' not null comment '사용여부',
    reg_id        bigint           null comment '등록자',
    reg_dt        datetime         null comment '등록일시',
    upd_id        bigint           null comment '수정자',
    upd_dt        datetime         null comment '수정일시'
);

ALTER TABLE tb_comm_code COMMENT='공통코드';