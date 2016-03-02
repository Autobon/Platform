CREATE TABLE `t_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_num` varchar(60) NOT NULL DEFAULT '' COMMENT '订单编号',
  `order_type` int(2) NOT NULL DEFAULT '0' COMMENT '订单类型(1-隔热膜 2-隐形车衣 3-车身改色 4-美容清洁)',
  `photo` varchar(255) DEFAULT NULL COMMENT '订单照片',
  `order_time` datetime DEFAULT NULL COMMENT '预约时间',
  `add_time` datetime DEFAULT NULL COMMENT '下单时间',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '订单状态 0-新建 1-已接单 2-已发出合作邀请等待结果 3-合作人已接受
      4-合作人已拒绝 5-工作中 6-已完成 7-已评价 20-已撤销',
  `creator_type` int(1) NOT NULL DEFAULT '0' COMMENT '下单人类型(1-合作商户 2-后台 3-用户)',
  `creator_id` int(11) DEFAULT NULL COMMENT '下单人编号',
  `creator_name` varchar(255) DEFAULT NULL COMMENT '下单人名称',
  `creator_phone` varchar(50) DEFAULT NULL COMMENT '下单人联系电话',
  `position_lon` varchar(20) DEFAULT NULL COMMENT '订单位置经度',
  `position_lat` varchar(20) DEFAULT NULL COMMENT '订单位置维度',
  `remark` varchar(255) DEFAULT NULL COMMENT '订单备注',
  `main_tech_id` int(11) NOT NULL DEFAULT '0' COMMENT '主技师id',
  `second_tech_id` int(11) NOT NULL DEFAULT '0' COMMENT '合作技师id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_order_num` (`order_num`)
) DEFAULT CHARSET=utf8 COMMENT='订单表';


INSERT INTO `t_order` (`id`,`order_num`,`order_type`,`photo`,`order_time`,`add_time`,`status`,`creator_type`,`creator_id`,`remark`,`main_tech_id`,`second_tech_id`) VALUES (1,'20160223134200014567',1,'','2016-02-24 14:00:00','2016-02-23 11:09:23',1,1,1,'bababala',1,2);
INSERT INTO `t_order` (`id`,`order_num`,`order_type`,`photo`,`order_time`,`add_time`,`status`,`creator_type`,`creator_id`,`remark`,`main_tech_id`,`second_tech_id`) VALUES (2,'20160223135200016789',2,NULL,'2016-02-24 14:00:00','2016-02-23 11:09:23',2,1,2,'somewords',2,1);

