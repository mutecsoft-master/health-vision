-- auto-generated definition
create table tb_role
(
    role_id bigint auto_increment comment 'ID'
        primary key,
    role_nm varchar(20)      null comment '역할명',
    del_yn  char default 'N' null comment '삭제여부(Y/N)'
)
    comment '역할';