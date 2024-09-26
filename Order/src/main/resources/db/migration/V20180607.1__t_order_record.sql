CREATE TABLE `t_order_status_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL COMMENT '订单ID',
  `status` int(2) DEFAULT NULL COMMENT '状态：0未接单，10已接单，20已签到，30施工前照片已上传，40施工中，50施工完成',
  `record_time` datetime DEFAULT NULL COMMENT '记录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `order_id_UNIQUE` (`order_id`),
  KEY `status_UNIQUE` (`status`),
  KEY `record_time_UNIQUE` (`record_time`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

