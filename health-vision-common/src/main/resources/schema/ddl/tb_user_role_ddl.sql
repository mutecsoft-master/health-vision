-- auto-generated definition
create table tb_user_role
(
    user_role_id bigint auto_increment comment 'ID'
        primary key,
    user_id      bigint null comment 'user id',
    role_id      bigint null comment 'role id',
    constraint tb_user_role_tb_role_role_id_fk
        foreign key (role_id) references tb_role (role_id),
    constraint tb_user_role_tb_user_user_id_fk
        foreign key (user_id) references tb_user (user_id)
)
    comment 'user role';