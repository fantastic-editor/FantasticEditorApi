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

-- 文档表
create table fy_document
(
    doc_uuid    varchar(32)                                 not null
        constraint fy_document_pk
            primary key,
    uuid        varchar(32)                                 not null
        constraint fy_document_fy_user_uuid_fk
            references fy_user
            on update cascade on delete cascade,
    type        smallint    default 0                       not null,
    key        varchar(64),
    cooperation varchar,
    suffix      varchar(20) default 'md'::character varying not null,
    created_at  timestamp   default now()                   not null,
    updated_at  timestamp
);

comment on table fy_document is '文档表';
comment on column fy_document.doc_uuid is '文档表 UUID（也对应 BOS 文档 UUID）';
comment on column fy_document.uuid is '所属用户';
comment on constraint fy_document_fy_user_uuid_fk on fy_document is '对应用户表 uuid';
comment on column fy_document.type is '所属类型（ 0:私有、1:链接私有[需要Key]、2:公开）';
comment on column fy_document.key is '文档密码';
comment on column fy_document.cooperation is '合作者(json 内存储用户 uuid 为 list 的形式)';
comment on column fy_document.suffix is '文档格式';
comment on column fy_document.created_at is '创建时间';
comment on column fy_document.updated_at is '修改时间';
