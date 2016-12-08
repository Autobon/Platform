CREATE TABLE `t_work_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL COMMENT '订单id',
  `tech_id` int(11) DEFAULT NULL COMMENT '技师id',
  `project1`int(2) default null comment '施工项目1',
  `position1` varchar(100) default null comment '施工部位1',
  `project2`int(2) default null comment '施工项目2',
  `position2` varchar(100) default null comment '施工部位2',
  `project3`int(2) default null comment '施工项目3',
  `position3` varchar(100) default null comment '施工部位3',
  `project4`int(2) default null comment '施工项目4',
  `position4` varchar(100) default null comment '施工部位4',
  `project5`int(2) default null comment '施工项目5',
  `position5` varchar(100) default null comment '施工部位5',
  `project6`int(2) default null comment '施工项目6',
  `position6` varchar(100) default null comment '施工部位6',
  `payment` FLOAT(8,2) DEFAULT '0' COMMENT '结算金额',
  `pay_status` int(11) DEFAULT '0' COMMENT '支付状态: 0-未出帐, 1-已出账, 2-已转账支付',
  `create_date` datetime DEFAULT null COMMENT '创建时间',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT='施工表';


