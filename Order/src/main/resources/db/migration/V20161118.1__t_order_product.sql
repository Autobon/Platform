CREATE TABLE `t_order_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL COMMENT '订单id',
  `construction_project_id` int(11) DEFAULT NULL COMMENT '技师id',
  `construction_position_id`int(4) default null comment '施工项目1',
  `product_id`int(4) default null comment '施工项目1',
  `construction_commission`int(2) default null comment '施工项目1',
  `scrap_cost`int(2) default null comment '施工项目1',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT='产品补录表';


