CREATE TABLE `t_function` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fun_name` varchar(100) NOT NULL COMMENT '功能名称',
  `fun_dir` varchar(500) DEFAULT NULL COMMENT '功能路径',
  `fun_type` varchar(10) DEFAULT NULL COMMENT '接口方式:POST PUT DELETE GET',
  `menu_id` int(10) DEFAULT '0' COMMENT '菜单ID',
  `category_id` int(10) DEFAULT '0' COMMENT '分类ID',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
)DEFAULT CHARSET=utf8 COMMENT='API表';


