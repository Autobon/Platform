CREATE TABLE `technician` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  skill varchar(45) COMMENT '技能',
  `name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '姓名',
  `gender` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '性别',
  `id_no` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '身份证号',
  `id_photo` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `avatar` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '头像',
  `bank` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '开户银行',
  `bank_address` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '开户行地址',
  `bank_card_no` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '银行卡号',
  `status` int(11) DEFAULT NULL COMMENT '0: 未审核，1：审核通过，2：审核未过，3：帐户禁用',
  `verify_at` datetime DEFAULT NULL COMMENT '审核时间',
  `last_login_at` datetime DEFAULT NULL COMMENT '上次登录时间',
  `last_login_ip` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上次登录IP',
  `create_at` datetime DEFAULT NULL,
  `star` int(11) DEFAULT NULL COMMENT '星级',
  `vote_rate` float DEFAULT NULL COMMENT '好评率',
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone_UNIQUE` (`phone`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `technician` VALUES (1,'18812345678','7c4a8d09ca3762af61e59520943dc26494f8941b',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,'2016-02-19 14:59:13','0:0:0:0:0:0:0:1','2016-02-18 00:00:00',0,0);
