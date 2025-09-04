-- auto-generated definition
create table tb_user
(
    user_id       bigint auto_increment comment 'ID NO'
        primary key,
    email         varchar(100) not null comment '이메일',
    user_pw       varchar(100) null comment '비밀번호',
    first_nm      varchar(255) not null comment '이름',
    last_nm       varchar(255) null comment '성',
    phone         varchar(20)  null comment '휴대폰번호',
    gender        char         null comment '성별(NULL/M/F)',
    birth_date    varchar(10)  null comment '생년월일(yyyy-MM-dd)',
    location      varchar(255) null comment '지역',
    sns_provider  varchar(100) null comment 'sns 로그인 종류',
    sns_user_id   varchar(255) null comment 'sns 사용자 식별자',
    last_login_dt datetime     null comment '마지막 로그인 일시',
    del_yn        char         null comment '탈퇴여부',
    del_dt        datetime     null comment '탈퇴일시',
    reg_dt        datetime     null comment '가입일시',
    upd_dt        datetime     null comment '수정일시',
    upd_id        bigint       null comment '수정자',
    constraint tb_user_pk
        unique (email)
)
    comment 'user';