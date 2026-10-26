-- =============================================
-- 物流配送管理系统 数据库初始化脚本
-- MySQL 5.7 / 8.0
-- 测试账号（密码统一为 123456，MD5 存储）：
--   管理员: admin      密码 123456
--   用户:   user1      密码 123456
--   配送员: courier1   密码 123456
-- =============================================
DROP DATABASE IF EXISTS logistics_db;
CREATE DATABASE logistics_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE logistics_db;

-- 1. 管理员表
CREATE TABLE `admin` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(64) NOT NULL COMMENT '密码(MD5)',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '姓名',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 2. 用户表
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(64) NOT NULL COMMENT '密码(MD5)',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '姓名',
  `gender` VARCHAR(10) DEFAULT NULL COMMENT '性别',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `balance` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '账户余额',
  `status` VARCHAR(10) NOT NULL DEFAULT '正常' COMMENT '状态: 正常/禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 3. 配送员表
CREATE TABLE `courier` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(64) NOT NULL COMMENT '密码(MD5)',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '姓名',
  `gender` VARCHAR(10) DEFAULT NULL COMMENT '性别',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `region_id` BIGINT DEFAULT NULL COMMENT '负责区域ID',
  `balance` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '可提现余额',
  `total_income` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '累计总收入',
  `order_count` INT NOT NULL DEFAULT 0 COMMENT '累计接单数量',
  `status` VARCHAR(10) NOT NULL DEFAULT '正常' COMMENT '状态: 正常/禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配送员表';

-- 4. 配送员认证表（普通用户申请认证为配送员，通用预约审核模型）
CREATE TABLE `courier_cert` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '申请人用户ID',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `id_card` VARCHAR(30) DEFAULT NULL COMMENT '身份证号',
  `region_id` BIGINT DEFAULT NULL COMMENT '申请负责区域ID',
  `status` VARCHAR(10) NOT NULL DEFAULT '待审核' COMMENT '状态: 待审核/已通过/已驳回',
  `reason` VARCHAR(255) DEFAULT NULL COMMENT '审核意见',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配送员认证表';

-- 5. 区域表
CREATE TABLE `region` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) NOT NULL COMMENT '区域名称',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='区域表';

-- 6. 网点表
CREATE TABLE `network_point` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(100) NOT NULL COMMENT '网点名称',
  `region_id` BIGINT DEFAULT NULL COMMENT '所属区域ID',
  `address` VARCHAR(255) DEFAULT NULL COMMENT '详细地址',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '网点图片',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '网点介绍',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流网点表';

-- 7. 寄件类别表
CREATE TABLE `category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) NOT NULL COMMENT '类别名称',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '类别描述',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='寄件类别表';

-- 8. 运费规则表
CREATE TABLE `freight_rule` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` BIGINT NOT NULL COMMENT '寄件类别ID',
  `first_weight` DECIMAL(10,2) NOT NULL DEFAULT 1.00 COMMENT '首重(kg)',
  `first_price` DECIMAL(10,2) NOT NULL COMMENT '首重价格(元)',
  `continued_price` DECIMAL(10,2) NOT NULL COMMENT '续重单价(元/kg)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运费规则表';

-- 9. 寄件订单表
CREATE TABLE `express_order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` VARCHAR(40) NOT NULL COMMENT '订单编号',
  `user_id` BIGINT NOT NULL COMMENT '寄件用户ID',
  `courier_id` BIGINT DEFAULT NULL COMMENT '接单配送员ID',
  `category_id` BIGINT DEFAULT NULL COMMENT '寄件类别ID',
  `network_point_id` BIGINT DEFAULT NULL COMMENT '寄件网点ID',
  `weight` DECIMAL(10,2) DEFAULT NULL COMMENT '重量(kg)',
  `amount` DECIMAL(10,2) DEFAULT NULL COMMENT '订单金额',
  `status` VARCHAR(10) NOT NULL DEFAULT '待接单' COMMENT '状态: 待接单/已接单/已送达/已签收',
  `sender_name` VARCHAR(50) DEFAULT NULL COMMENT '寄件人姓名',
  `sender_phone` VARCHAR(20) DEFAULT NULL COMMENT '寄件人电话',
  `sender_address` VARCHAR(255) DEFAULT NULL COMMENT '寄件地址',
  `receiver_name` VARCHAR(50) DEFAULT NULL COMMENT '收件人姓名',
  `receiver_phone` VARCHAR(20) DEFAULT NULL COMMENT '收件人电话',
  `receiver_address` VARCHAR(255) DEFAULT NULL COMMENT '收件地址',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `accept_time` DATETIME DEFAULT NULL COMMENT '接单时间',
  `sign_time` DATETIME DEFAULT NULL COMMENT '签收时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_courier_id` (`courier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='寄件订单表';

-- 10. 评价表
CREATE TABLE `evaluation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '评价用户ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `courier_id` BIGINT DEFAULT NULL COMMENT '被评价配送员ID',
  `score` INT NOT NULL DEFAULT 5 COMMENT '评分(1-5)',
  `content` VARCHAR(500) DEFAULT NULL COMMENT '评价内容',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- 11. 系统公告表（内容为富文本）
CREATE TABLE `announcement` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(100) NOT NULL COMMENT '公告标题',
  `content` TEXT COMMENT '公告内容(富文本)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统公告表';

-- 12. 物流资讯表（内容为富文本）
CREATE TABLE `news` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(100) NOT NULL COMMENT '资讯标题',
  `cover` VARCHAR(255) DEFAULT NULL COMMENT '封面图片',
  `content` TEXT COMMENT '资讯内容(富文本)',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览量',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流资讯表';

-- 13. 我的地址表
CREATE TABLE `address` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `name` VARCHAR(50) NOT NULL COMMENT '联系人姓名',
  `phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
  `address` VARCHAR(255) NOT NULL COMMENT '详细地址',
  `is_default` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否默认地址',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='我的地址表';

-- 14. 我的收藏表（收藏网点）
CREATE TABLE `favorite` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `network_point_id` BIGINT NOT NULL COMMENT '网点ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_point` (`user_id`, `network_point_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='我的收藏表';

-- 15. 消息提醒表
CREATE TABLE `message` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '接收人ID',
  `role` VARCHAR(10) NOT NULL COMMENT '接收人角色: USER/COURIER',
  `title` VARCHAR(100) DEFAULT NULL COMMENT '消息标题',
  `content` VARCHAR(500) DEFAULT NULL COMMENT '消息内容',
  `order_id` BIGINT DEFAULT NULL COMMENT '关联订单ID',
  `is_read` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息提醒表';

-- 16. 充值记录表
CREATE TABLE `recharge_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '充值金额',
  `status` VARCHAR(10) NOT NULL DEFAULT '成功' COMMENT '状态: 成功',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '充值时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值记录表';

-- 17. 提现记录表
CREATE TABLE `withdraw_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `courier_id` BIGINT NOT NULL COMMENT '配送员ID',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '提现金额',
  `status` VARCHAR(10) NOT NULL DEFAULT '待审核' COMMENT '状态: 待审核/已通过/已驳回',
  `reason` VARCHAR(255) DEFAULT NULL COMMENT '审核意见',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提现记录表';

-- =============================================
-- 初始化数据（密码均为 123456 的 MD5）
-- =============================================
INSERT INTO `admin` (`id`, `username`, `password`, `name`, `phone`, `email`) VALUES
(1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', '13900000001', 'admin@logistics.com');

INSERT INTO `user` (`id`, `username`, `password`, `name`, `gender`, `phone`, `balance`) VALUES
(1, 'user1', 'e10adc3949ba59abbe56e057f20f883e', '张三', '男', '13800000001', 200.00),
(2, 'user2', 'e10adc3949ba59abbe56e057f20f883e', '李四', '女', '13800000002', 50.00);

INSERT INTO `courier` (`id`, `username`, `password`, `name`, `gender`, `phone`, `region_id`, `balance`, `total_income`, `order_count`) VALUES
(1, 'courier1', 'e10adc3949ba59abbe56e057f20f883e', '王五', '男', '13700000001', 1, 80.00, 230.00, 5);

INSERT INTO `region` (`id`, `name`) VALUES
(1, '华东区'), (2, '华南区'), (3, '华北区');

INSERT INTO `network_point` (`id`, `name`, `region_id`, `address`, `phone`, `description`) VALUES
(1, '人民路营业网点', 1, '某某市人民路88号', '0510-88880001', '位于市中心，交通便利，营业时间 8:00-20:00'),
(2, '科技城营业网点', 1, '某某市科技城大道12号', '0510-88880002', '覆盖科技城片区，支持大件收寄'),
(3, '滨江营业网点', 2, '某某市滨江路199号', '020-88880003', '临江网点，寄件高峰期为午后'),
(4, '天河营业网点', 2, '某某市天河路66号', '020-88880004', '商圈核心网点，日均寄件量大'),
(5, '朝阳门营业网点', 3, '某某市朝阳门外大街5号', '010-88880005', '毗邻交通枢纽，收寄便捷');

INSERT INTO `category` (`id`, `name`, `description`) VALUES
(1, '文件证件', '文件、合同、证件等纸质物品'),
(2, '日用品', '日常生活用品'),
(3, '生鲜食品', '水果、生鲜等需快速配送物品'),
(4, '电子产品', '手机、电脑及配件等'),
(5, '服饰鞋包', '服装、鞋帽、箱包等');

INSERT INTO `freight_rule` (`id`, `category_id`, `first_weight`, `first_price`, `continued_price`) VALUES
(1, 1, 1.00, 8.00, 2.00),
(2, 2, 1.00, 10.00, 3.00),
(3, 3, 1.00, 15.00, 5.00),
(4, 4, 1.00, 12.00, 4.00),
(5, 5, 1.00, 10.00, 3.00);

INSERT INTO `announcement` (`id`, `title`, `content`) VALUES
(1, '系统上线公告', '<p>物流配送管理系统正式上线，欢迎广大用户使用！寄件前请先完成账户充值，寄件金额将从账户余额中扣除。</p>'),
(2, '旺季配送时效提醒', '<p>近期为物流旺季，部分区域配送时效可能延长，请合理安排寄件时间，感谢您的理解与支持。</p>');

INSERT INTO `news` (`id`, `title`, `content`, `view_count`) VALUES
(1, '智慧物流助力双十一', '<p>今年双十一期间，全国多地启用智慧分拣中心，分拣效率提升 40%。</p>', 156),
(2, '绿色包装推广行动启动', '<p>多家物流企业联合发起绿色包装行动，推广可循环快递箱。</p>', 98),
(3, '同城半日达服务扩容', '<p>同城半日达服务已覆盖更多城市，寄件更快捷。</p>', 73);

INSERT INTO `address` (`id`, `user_id`, `name`, `phone`, `address`, `is_default`) VALUES
(1, 1, '张三', '13800000001', '某某市人民路100号3栋502', 1),
(2, 1, '李四', '13800000002', '某某市科技城大道8号写字楼A座', 0);

INSERT INTO `favorite` (`id`, `user_id`, `network_point_id`) VALUES
(1, 1, 1), (2, 1, 2);

INSERT INTO `courier_cert` (`id`, `user_id`, `name`, `phone`, `id_card`, `region_id`) VALUES
(1, 2, '李四', '13800000002', '320000199001010011', 1);

-- 近一个月寄件订单（用于首页看板统计图表）
INSERT INTO `express_order` (`order_no`, `user_id`, `courier_id`, `category_id`, `network_point_id`, `weight`, `amount`, `status`, `sender_name`, `sender_phone`, `sender_address`, `receiver_name`, `receiver_phone`, `receiver_address`, `create_time`, `accept_time`, `sign_time`) VALUES
('EX20260928001', 1, 1, 1, 1, 1.00, 8.00,  '已签收', '张三', '13800000001', '某某市人民路100号', '李四', '13800000002', '某某市科技城大道8号', DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_SUB(NOW(), INTERVAL 23 DAY)),
('EX20261002002', 2, 1, 2, 3, 3.50, 19.00, '已签收', '李四', '13800000002', '某某市滨江路20号', '张三', '13800000001', '某某市人民路100号', DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 19 DAY)),
('EX20261006003', 1, 1, 3, 2, 2.00, 20.00, '已签收', '张三', '13800000001', '某某市科技城大道8号', '王芳', '13800000003', '某某市天河路66号', DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY)),
('EX20261011004', 2, 1, 4, 4, 1.00, 12.00, '已签收', '李四', '13800000002', '某某市天河路12号', '张三', '13800000001', '某某市朝阳门外大街5号', DATE_SUB(NOW(), INTERVAL 11 DAY), DATE_SUB(NOW(), INTERVAL 11 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY)),
('EX20261015005', 1, 1, 5, 1, 4.00, 22.00, '已签收', '张三', '13800000001', '某某市人民路100号', '李四', '13800000002', '某某市滨江路20号', DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY)),
('EX20261019006', 1, 1, 1, 5, 2.00, 12.00, '已送达', '张三', '13800000001', '某某市人民路100号', '李四', '13800000002', '某某市人民路88号', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), NULL),
('EX20261021007', 2, NULL, 2, 2, 1.00, 10.00, '待接单', '李四', '13800000002', '某某市科技城大道8号', '张三', '13800000001', '某某市人民路100号', DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, NULL),
('EX20261022008', 1, NULL, 4, 4, 0.50, 12.00, '待接单', '张三', '13800000001', '某某市天河路12号', '李四', '13800000002', '某某市科技城大道8号', NOW(), NULL, NULL);

INSERT INTO `evaluation` (`id`, `user_id`, `order_id`, `courier_id`, `score`, `content`) VALUES
(1, 1, 1, 1, 5, '配送员服务态度很好，送达速度快！'),
(2, 2, 2, 1, 4, '整体不错，包装完整。');

INSERT INTO `message` (`user_id`, `role`, `title`, `content`, `order_id`) VALUES
(1, 'USER', '寄件下单成功', '您的订单 EX20261022008 已创建，等待配送员接单。', 8),
(2, 'USER', '寄件下单成功', '您的订单 EX20261021007 已创建，等待配送员接单。', 7);

INSERT INTO `recharge_record` (`id`, `user_id`, `amount`) VALUES
(1, 1, 100.00), (2, 1, 100.00);

INSERT INTO `withdraw_record` (`courier_id`, `amount`, `status`) VALUES
(1, 50.00, '待审核');
