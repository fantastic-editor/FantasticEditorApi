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

-- 生成付费会员表
create table fy_vip
(
    vuuid        varchar(32)                  not null
        constraint fy_vip_pk
            primary key,
    name         varchar(30)                  not null,
    display_name varchar(30)                  not null,
    price        decimal(10, 2) default 0     not null,
    description  varchar                      not null,
    updated_at   timestamp      default now() not null
);

comment on table fy_vip is '付费会员表';
comment on column fy_vip.vuuid is '会员 uuid 主键表';
comment on column fy_vip.name is '会员字段';
comment on column fy_vip.display_name is '展示名字';
comment on column fy_vip.price is '价格';
comment on column fy_vip.description is '展示名字';
comment on column fy_role.updated_at is '更新时间';

create unique index fy_vip_name_uindex
    on fy_vip (name);

