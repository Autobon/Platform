CREATE TABLE `t_bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tech_id` int(11) DEFAULT NULL,
  `year_month` datetime DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `sum` float(8,2) DEFAULT NULL,
  `payed` tinyint(1) DEFAULT '0' COMMENT '是否已支付',
  `pay_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `indx_bill_sum` (`sum`),
  KEY `indx_bill_tech_id` (`tech_id`),
  KEY `indx_bill_year_month` (`year_month`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
