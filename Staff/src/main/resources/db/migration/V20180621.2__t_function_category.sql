CREATE TABLE `t_function_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(50) NOT NULL COMMENT '类别名称',
  `menu_id` int(11) DEFAULT NULL COMMENT '菜单ID',
  `is_default` int(1) DEFAULT NULL COMMENT '是否默认  0非默认 1默认',
  PRIMARY KEY (`id`),
  UNIQUE KEY `category` (`category`)
) ENGINE=InnoDB AUTO_INCREMENT=291 DEFAULT CHARSET=utf8 COMMENT='权限分类表';


