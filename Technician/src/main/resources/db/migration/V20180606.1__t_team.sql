CREATE TABLE `t_team` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '姓名',
  `manager_id` int(11) DEFAULT NULL COMMENT '负责人id',
  `manager_name` VARCHAR(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '负责人姓名',
  `manager_phone` VARCHAR(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '负责人电话',
  PRIMARY KEY (`id`),
  UNIQUE KEY `manager_id_UNIQUE` (`manager_id`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
