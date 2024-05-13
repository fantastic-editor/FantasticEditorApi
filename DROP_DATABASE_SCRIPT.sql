-- 终止所有对 'fantasticeditor' 数据库的连接
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'fantasticeditor'
  AND pid <> pg_backend_pid();

-- 撤销用户对 public schema 的所有权限
REVOKE ALL ON SCHEMA public FROM fantasticeditor;

-- 撤销用户对 fantasticeditor 数据库的所有权限
REVOKE ALL PRIVILEGES ON DATABASE fantasticeditor FROM fantasticeditor;

-- 删除用户
DROP USER fantasticeditor;

-- 删除数据库
DROP DATABASE fantasticeditor;