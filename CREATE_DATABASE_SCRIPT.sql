-- 数据库创建脚本
-- 创建数据库 fantasticeditor
-- 创建用户 fantasticeditor
-- 用户密码 fantasticeditor
-- 赋予用户 fantasticeditor 对数据库 fantasticeditor 的所有权限
CREATE USER fantasticeditor WITH PASSWORD 'fantasticeditor';
CREATE DATABASE fantasticeditor;
GRANT ALL PRIVILEGES ON DATABASE fantasticeditor TO fantasticeditor;