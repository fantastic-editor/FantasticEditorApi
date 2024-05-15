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

-- 生成用户表
create table fy_user
(
    uuid              varchar(32)             not null
        constraint fy_user_pk
            primary key,
    username          varchar(40)             not null,
    email             varchar(100)            not null,
    phone             varchar(11)             not null,
    password          varchar(60)             not null,
    old_password      varchar(60),
    avatar            varchar(64),
    otp_auth          varchar(32)             not null,
    mail_verify       boolean   default false not null,
    phone_verify      boolean   default false not null,
    basic_information json                    not null,
    role              varchar(32)             not null
        constraint fy_user_fy_role_ruuid_fk
            references fy_role
            on update cascade on delete set null,
    vip               varchar(32)
        constraint fy_user_fy_vip_vuuid_fk
            references fy_vip
            on update cascade on delete set null,
    created_at        timestamp default now() not null,
    updated_at        timestamp
);

comment on table fy_user is '用户表';
comment on column fy_user.uuid is '用户主键';
comment on column fy_user.username is '用户名';
comment on column fy_user.email is '邮箱';
comment on column fy_user.phone is '手机号码';
comment on column fy_user.password is '密码';
comment on column fy_user.old_password is '旧密码';
comment on column fy_user.avatar is '头像（非地址）';
comment on column fy_user.otp_auth is '2FA-OTPAuth';
comment on column fy_user.mail_verify is '邮箱验证';
comment on column fy_user.phone_verify is '手机验证';
comment on column fy_user.basic_information is '基础信息';
comment on column fy_user.role is '用户角色';
comment on constraint fy_user_fy_role_ruuid_fk on fy_user is 'role 表 uuid 外键约束';
comment on column fy_user.vip is '付费会员';
comment on constraint fy_user_fy_vip_vuuid_fk on fy_user is 'vip 表 vuuid 外键约束';
comment on column fy_user.created_at is '创建时间';
comment on column fy_user.updated_at is '修改时间';

create unique index fy_user_email_uindex
    on fy_user (email);
create unique index fy_user_otp_auth_uindex
    on fy_user (otp_auth);
create unique index fy_user_phone_uindex
    on fy_user (phone);
create unique index fy_user_username_uindex
    on fy_user (username);

