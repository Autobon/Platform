CREATE TABLE `t_tech_finance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tech_id` int(11) DEFAULT NULL COMMENT '技师id',
  `sum_income` decimal(10,2) DEFAULT NULL COMMENT '总收入',
  `sum_cash` decimal(10,2) DEFAULT NULL COMMENT '已提现',
  `not_cash` decimal(10,2) DEFAULT NULL COMMENT '未提现',
  `already_apply` decimal(10,2) DEFAULT NULL COMMENT '已申请未提现',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=451 DEFAULT CHARSET=utf8 COMMENT='技师流水表';