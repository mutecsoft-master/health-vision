-- auto-generated definition
create table tb_board
(
    board_id bigint auto_increment comment 'ID'
        primary key,
    title    varchar(255)     null comment '제목',
    content  text             null comment '내용',
    del_yn   char default 'N' null comment '삭제여부',
    view_cnt int  default 0   null comment '조회수',
    reg_id   bigint           null comment '등록자',
    reg_dt   datetime         null comment '등록일시',
    upd_id   bigint           null comment '수정자',
    upd_dt   datetime         null comment '수정일시'
)
    comment '게시판';

