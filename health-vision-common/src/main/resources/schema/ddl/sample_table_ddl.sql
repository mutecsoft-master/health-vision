-- auto-generated definition
create table sample_table
(
    id     varchar(100)                         null,
    name   varchar(100)                         null,
    reg_id varchar(100)                         null,
    reg_dt datetime default current_timestamp() null,
    upd_id varchar(100)                         null,
    upd_dt datetime default current_timestamp() null
);