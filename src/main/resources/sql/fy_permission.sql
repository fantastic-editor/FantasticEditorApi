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

-- 创建权限表
create table fy_permission
(
    pid         bigserial
        constraint fy_permission_pk
            primary key,
    permission  varchar not null,
    description varchar not null
);

comment on table fy_permission is '权限表';
comment on column fy_permission.pid is '权限主键';
comment on column fy_permission.permission is '权限字段';
comment on column fy_permission.description is '权限描述';

create unique index fy_permission_permission_uindex
    on fy_permission (permission);

