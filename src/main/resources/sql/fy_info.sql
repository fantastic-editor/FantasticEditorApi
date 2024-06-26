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

