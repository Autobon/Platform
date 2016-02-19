CREATE TABLE `t_evaluate` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL COMMENT '订单id',
  `star_level` int(1) DEFAULT NULL COMMENT '评价星个数',
  `check_options` varchar(100) DEFAULT NULL COMMENT '勾选项(逗号隔开)',
  `advice` varchar(255) DEFAULT NULL COMMENT '评价意见和建议',
  PRIMARY KEY (`Id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `t_evaluate_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单评价表';
