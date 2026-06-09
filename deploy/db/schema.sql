-- --------------------------------------------------------
-- 基础数据查询服务 数据库初始化脚本
-- --------------------------------------------------------

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `jwbasedata` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `jwbasedata`;

-- --------------------------------------------------------
-- 用户表
-- --------------------------------------------------------
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_username` (`username`),
    KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- --------------------------------------------------------
-- 测试数据
-- --------------------------------------------------------
INSERT INTO `user` (`username`, `email`, `created_at`) VALUES
('zhangsan', 'zhangsan@example.com', NOW()),
('lisi', 'lisi@example.com', NOW()),
('wangwu', 'wangwu@example.com', NOW()),
('zhaoliu', 'zhaoliu@example.com', NOW()),
('sunqi', 'sunqi@example.com', NOW()),
('zhouba', 'zhouba@example.com', NOW()),
('wujiu', 'wujiu@example.com', NOW()),
('zhengshi', 'zhengshi@example.com', NOW()),
('wangshiyi', 'wangshiyi@example.com', NOW()),
('lishier', 'lishier@example.com', NOW());
