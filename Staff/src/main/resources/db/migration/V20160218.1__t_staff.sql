CREATE TABLE `t_staff` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '密码',
  `email` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_at` datetime DEFAULT NULL COMMENT '注册时间',
  `last_login_at` datetime DEFAULT NULL,
  `last_login_ip` varchar(125) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` int(11) DEFAULT '1' COMMENT '0: 禁用， 1： 可用',
  `role` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `staff_username_UNIQUE` (`username`),
  UNIQUE KEY `staff_email_UNIQUE` (`email`),
  UNIQUE KEY `staff_phone_UNIQUE` (`phone`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='员工登陆账户表';

INSERT INTO `t_staff` (id, username, password, email, phone, name, create_at, last_login_at, last_login_ip, status, role) values (
'1', 'admin', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'huangdc@incardata.com.cn', '15107116464', '超级管理员', '2016-03-01 12:45:00', NULL, '127.0.0.1', 1, 'SUPER');
