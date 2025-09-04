-- auto-generated definition
create table tb_menu
(
    menu_id    bigint auto_increment comment 'ID'
        primary key,
    menu_nm    varchar(100) not null comment '메뉴명',
    url        varchar(255) null comment 'url',
    parent_id  bigint       null comment '부모메뉴ID',
    sort_order int          null comment '정렬순서',
    use_yn     char         null comment '사용여부',
    del_yn     char         null comment '삭제여부',
    reg_dt     datetime     null comment '등록일시',
    reg_id     bigint       null comment '등록자',
    upd_dt     datetime     null comment '수정일시',
    upd_id     bigint       null comment '수정자'
)
    comment '메뉴';

