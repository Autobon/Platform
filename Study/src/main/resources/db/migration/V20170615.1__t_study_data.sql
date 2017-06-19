CREATE TABLE `t_study_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) COMMENT '1: 培训资料， 2： 施工标准， 3： 业务规则',
  `file_name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `file_length` int(11) DEFAULT '0' COMMENT '文件大小',
  `path` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `remark` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='学习资料';

