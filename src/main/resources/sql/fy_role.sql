/*
 * *******************************************************************************
 * Copyright (C) 2024-NOW(至今) 妙笔智编
 * Author: 锋楪技术团队
 *
 * 本文件包含 妙笔智编「FantasticEditor」 的源代码，该项目的所有源代码均遵循MIT开源许可证协议。
 * 本代码仅允许在十三届软件杯比赛授权比赛方可直接使用
 * *******************************************************************************
 * 免责声明：
 * 使用本软件的风险由用户自担。作者或版权持有人在法律允许的最大范围内，
 * 对因使用本软件内容而导致的任何直接或间接的损失不承担任何责任。
 * *******************************************************************************
 */

-- 生成角色表
create table fy_role
(
    ruuid        varchar(32)             not null
        constraint fy_role_pk
            primary key,
    name         varchar(30)             not null,
    display_name varchar(30)             not null,
    description  varchar                 not null,
    permissions  varchar                 not null,
    updated_at   timestamp default now() not null
);

comment on table fy_role is '角色表';
comment on column fy_role.ruuid is '角色主键';
comment on column fy_role.name is '角色名字';
comment on column fy_role.display_name is '展示名字';
comment on column fy_role.description is '角色描述';
comment on column fy_role.permissions is '角色权限';
comment on column fy_role.updated_at is '更新时间';

create unique index fy_role_role_uindex
    on fy_role (name);



