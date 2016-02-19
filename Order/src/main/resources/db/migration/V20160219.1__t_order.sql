CREATE TABLE `t_order` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `order_num` varchar(60) NOT NULL DEFAULT '' COMMENT '订单编号',
  `order_type` int(2) DEFAULT NULL COMMENT '订单类型',
  `photo` varchar(255) DEFAULT NULL COMMENT '订单照片',
  `order_time` datetime DEFAULT NULL COMMENT '预约时间',
  `add_time` datetime DEFAULT NULL COMMENT '下单时间',
  `status` int(1) DEFAULT NULL COMMENT '订单状态 0-未接单 1-已接单 2-工作中 3-已完成 4-已评价',
  `customer_type` int(1) DEFAULT NULL COMMENT '客户类型',
  `customer_id` int(11) DEFAULT NULL COMMENT '下单客户编号',
  `remark` varchar(255) DEFAULT NULL COMMENT '订单备注',
  PRIMARY KEY (`Id`),
  UNIQUE KEY unique_order_num (order_num)
) DEFAULT CHARSET=utf8 COMMENT='订单表';