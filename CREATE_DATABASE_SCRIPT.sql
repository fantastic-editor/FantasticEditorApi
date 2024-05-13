-- 数据库创建脚本
-- 创建数据库 fantasticeditor
-- 创建用户 fantasticeditor
-- 用户密码 fantasticeditor
-- 赋予用户 fantasticeditor 对数据库 fantasticeditor 的所有权限
-- 赋予用户 fantasticeditor 对 public 模式的使用权限
CREATE USER fantasticeditor WITH PASSWORD 'fantasticeditor';
CREATE DATABASE fantasticeditor;
ALTER SCHEMA public OWNER TO fantasticeditor;
ALTER DATABASE fantasticeditor OWNER TO fantasticeditor;
