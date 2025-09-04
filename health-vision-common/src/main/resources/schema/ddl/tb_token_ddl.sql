create table tb_token
(
    token_id bigint auto_increment comment 'ID' primary key,
    user_id bigint not null comment '사용자 ID',
    token_type varchar(20) not null comment '토큰 타입',
    token varchar(1000) not null comment '토큰',
    reg_dt datetime comment '등록일시',
    constraint tb_token_tb_user_fk foreign key (user_id) references tb_user (user_id)
)

ALTER TABLE tb_token comment '토큰';