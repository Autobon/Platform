CREATE TABLE `t_construction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL COMMENT '订单id',
  `tech_id` int(11) DEFAULT NULL COMMENT '技师id',
  `position_lon` varchar(20) DEFAULT NULL COMMENT '实时位置经度',
  `position_lat` varchar(20) DEFAULT NULL COMMENT '实时位置纬度',
  `start_time` datetime DEFAULT NULL COMMENT '施工开始时间',
  `signin_time` datetime DEFAULT NULL COMMENT '签到时间',
  `end_time` datetime DEFAULT NULL COMMENT '施工结束时间',
  `before_photos` varchar(1024) DEFAULT NULL COMMENT '施工前照片数组,用逗号隔开',
  `after_photos` varchar(2048) DEFAULT NULL COMMENT '施工后照片数组,用逗号隔开',
  `payment` FLOAT(8,2) DEFAULT '0' COMMENT '结算金额',
  `work_items` varchar(100) DEFAULT NULL COMMENT '工作项（多项用逗号隔开）',
  `work_percent` FLOAT(5,3) DEFAULT '0' COMMENT '工作占比, 用小数表示, 如0.2',
  `car_seat` int(11) DEFAULT '5' COMMENT '车辆座位数,5或7',
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `tech_id` (`tech_id`)
) DEFAULT CHARSET=utf8 COMMENT='施工表';