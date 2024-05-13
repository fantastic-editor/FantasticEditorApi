-- 软件信息表
create table fy_info
(
    id         char(36)                not null
        constraint fy_info_pk
            primary key,
    key        varchar                 not null,
    value      varchar                 not null,
    updated_at timestamp default now() not null
);

comment on table fy_info is '软件信息表';
comment on column fy_info.id is '主键';
comment on column fy_info.key is '键';
comment on column fy_info.value is '值';
comment on column fy_info.updated_at is '数据更新时间';

create unique index fy_info_key_uindex
    on fy_info (key);

