CREATE TABLE `t_merchandiser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` VARCHAR(45) COLLATE utf8_unicode_ci NOT NULL,
  `password` VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` VARCHAR(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '姓名',
  `gender` VARCHAR(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '性别',
  `id_no` VARCHAR(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '身份证号',
  `id_photo` VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `avatar` VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '头像',
  `bank` VARCHAR(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '开户银行',
  `bank_address` VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '开户行地址',
  `bank_card_no` VARCHAR(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '银行卡号',
  `status` int(11) DEFAULT NULL COMMENT '0: 未审核，1：审核通过，2：审核未过，3：帐户禁用',
  `last_login_at` datetime DEFAULT NULL COMMENT '上次登录时间',
  `last_login_ip` VARCHAR(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上次登录IP',
  `create_at` datetime DEFAULT NULL,
  `push_id` VARCHAR(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone_UNIQUE` (`phone`),
  KEY `tech_name_index` (`name`),
  KEY `tech_push_id` (`push_id`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

