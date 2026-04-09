-- AI智能待办助手 - 数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS ai_help CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ai_help;

-- ----------------------------
-- 1. 用户表
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名（唯一）',
    `password` VARCHAR(100) NOT NULL COMMENT '密码（BCrypt加密）',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- 2. 任务分类表
-- ----------------------------
DROP TABLE IF EXISTS `task_category`;
CREATE TABLE `task_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID（外键）',
    `category_name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `category_color` VARCHAR(20) DEFAULT '#409EFF' COMMENT '分类颜色',
    `sort_order` INT DEFAULT 0 COMMENT '排序顺序',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务分类表';

-- ----------------------------
-- 3. 待办任务表
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID（外键）',
    `category_id` BIGINT DEFAULT NULL COMMENT '分类ID（外键，可为NULL）',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父任务ID（用于子任务/拆解任务）',
    `title` VARCHAR(200) NOT NULL COMMENT '任务标题',
    `description` TEXT COMMENT '任务描述',
    `priority` VARCHAR(10) DEFAULT 'middle' COMMENT '优先级：high/middle/low',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-未完成，1-已完成',
    `deadline` DATETIME DEFAULT NULL COMMENT '截止时间',
    `reminder_time` DATETIME DEFAULT NULL COMMENT '提醒时间',
    `completed_time` DATETIME DEFAULT NULL COMMENT '完成时间',
    `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签，JSON数组',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`),
    KEY `idx_deadline` (`deadline`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='待办任务表';

-- ----------------------------
-- 初始化默认分类
-- ----------------------------
INSERT INTO `task_category` (`user_id`, `category_name`, `category_color`, `sort_order`) VALUES
(1, '工作', '#F56C6C', 1),
(1, '学习', '#409EFF', 2),
(1, '生活', '#67C23A', 3),
(1, '其他', '#909399', 4);
