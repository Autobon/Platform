CREATE TABLE `t_construction_waste` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL COMMENT '订单id',
  `tech_id` int(11) DEFAULT NULL COMMENT '技师id',
  `construction_project` int(2) DEFAULT NULL COMMENT '项目名称',
  `construction_position` int(2) DEFAULT NULL COMMENT '施工部位',
  `total` int(2) DEFAULT 0 COMMENT '数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8  COMMENT='施工报废明细';;
