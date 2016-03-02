CREATE TABLE `t_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_num` varchar(60) NOT NULL DEFAULT '' COMMENT '订单编号',
  `order_type` int(2) NOT NULL DEFAULT '0' COMMENT '订单类型(1-隔热膜 2-隐形车衣 3-车身改色 4-美容清洁)',
  `photo` varchar(255) DEFAULT NULL COMMENT '订单照片',
  `order_time` datetime DEFAULT NULL COMMENT '预约时间',
  `add_time` datetime DEFAULT NULL COMMENT '下单时间',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '订单状态 0-未接单 1-已接单 (-1)-合作邀请已拒绝 2-工作中 3-已完成 4-已评价 5-已撤销',
  `customer_type` int(1) NOT NULL DEFAULT '0' COMMENT '客户类型(1-合作商户 2-后台 3-用户)',
  `customer_id` int(11) DEFAULT NULL COMMENT '下单客户编号',
  `customer_name` varchar(255) DEFAULT NULL COMMENT '下单客户名称',
  `customer_lon` varchar(20) DEFAULT NULL COMMENT '客户位置经度',
  `customer_lat` varchar(20) DEFAULT NULL COMMENT '客户位置维度',
  `remark` varchar(255) DEFAULT NULL COMMENT '订单备注',
  `main_tech_id` int(11) NOT NULL DEFAULT '0' COMMENT '主技师id',
  `second_tech_id` int(11) NOT NULL DEFAULT '0' COMMENT '合作技师id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_order_num` (`order_num`)
)DEFAULT CHARSET=utf8 COMMENT='订单表';


INSERT INTO `t_order` (`id`,`order_num`,`order_type`,`photo`,`order_time`,`add_time`,`status`,`customer_type`,`customer_id`,`remark`,`main_tech_id`,`second_tech_id`) VALUES (1,'20160223134200014567',1,'','2016-02-24 14:00:00','2016-02-23 11:09:23',1,1,1,'bababala',2,1);
INSERT INTO `t_order` (`id`,`order_num`,`order_type`,`photo`,`order_time`,`add_time`,`status`,`customer_type`,`customer_id`,`remark`,`main_tech_id`,`second_tech_id`) VALUES (2,'20160223135200016789',2,NULL,'2016-02-24 14:00:00','2016-02-23 11:09:23',2,1,2,'somewords',1,2);
