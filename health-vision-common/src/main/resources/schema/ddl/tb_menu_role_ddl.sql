-- auto-generated definition
create table tb_menu_role
(
    menu_role_id bigint auto_increment comment 'ID'
        primary key,
    menu_id      bigint not null comment '메뉴 id',
    role_id      bigint not null comment '역할 id',
    constraint tb_menu_role_tb_menu_menu_id_fk
        foreign key (menu_id) references tb_menu (menu_id),
    constraint tb_menu_role_tb_role_role_id_fk
        foreign key (role_id) references tb_role (role_id)
)
    comment '메뉴별 역할';

