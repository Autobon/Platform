CREATE TABLE `t_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(1) NOT NULL COMMENT '施工项目',
  `brand` varchar(20) NOT NULL DEFAULT '品牌',
  `code` varchar(30) NOT NULL DEFAULT '编码',
  `model` varchar(20) NOT NULL DEFAULT '型号',
  `construction_position` int(3) NOT NULL COMMENT '施工部位',
  `working_hours` int(5) NOT NULL COMMENT '工时',
  `construction_commission` int(4) unsigned NOT NULL COMMENT '施工提成',
  `star_level` int(1) DEFAULT NULL COMMENT '星级要求',
  `scrap_cost` int(4) DEFAULT NULL COMMENT '报废扣款',
  `warranty` int(3) DEFAULT NULL COMMENT '质保',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8  COMMENT='产品表';;

