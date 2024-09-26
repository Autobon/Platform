CREATE TABLE `t_merchandiser_cooperator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchandiser_id` int(11) DEFAULT NULL COMMENT '跟单员ID',
  `cooperator_id` int(11) DEFAULT NULL COMMENT '商户ID',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

