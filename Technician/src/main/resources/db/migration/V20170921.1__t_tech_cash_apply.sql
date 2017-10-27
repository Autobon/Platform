CREATE TABLE `t_tech_cash_apply` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apply_date` datetime DEFAULT NULL COMMENT '申请时间',
  `apply_money` decimal(8,2) DEFAULT '0.00' COMMENT '申请金额',
  `tech_id` int(11) NOT NULL DEFAULT '0' COMMENT '提现技师id',
  `pay_date` datetime DEFAULT NULL COMMENT '支付时间',
  `payment` decimal(8,2) DEFAULT '0.00' COMMENT '支付金额',
  `not_pay` decimal(8,2) DEFAULT '0.00' COMMENT '支付金额',
  `state` int(1) DEFAULT '0' COMMENT '支付状态  0已申请，1部分扣款，2已扣款, 3已被取消',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=463 DEFAULT CHARSET=utf8 COMMENT='提现申请表';