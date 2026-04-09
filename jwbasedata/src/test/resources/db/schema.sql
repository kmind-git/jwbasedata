-- H2兼容的数据库初始化脚本
-- 注意：移除了H2不支持的CREATE DATABASE和USE语句

-- 用户表
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
);

-- 创建索引（H2兼容语法）
CREATE INDEX IF NOT EXISTS `idx_username` ON `user` (`username`);
CREATE INDEX IF NOT EXISTS `idx_email` ON `user` (`email`);