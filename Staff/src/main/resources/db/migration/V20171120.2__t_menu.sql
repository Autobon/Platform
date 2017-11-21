CREATE TABLE `t_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '菜单名称',
  `remark` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='菜单表';


insert into t_menu values(1, '首页', '首页');
insert into t_menu values(2, '订单管理', '订单管理');
insert into t_menu values(3, '商户管理', '商户管理');
insert into t_menu values(4, '技师管理', '技师管理');
insert into t_menu values(5, '财务管理', '财务管理');
insert into t_menu values(6, '产品管理', '产品管理');
insert into t_menu values(7, '统计分析', '统计分析');
insert into t_menu values(8, '学习园地', '学习园地');
insert into t_menu values(9, '地图模式', '地图模式');
insert into t_menu values(10, '用户角色', '用户角色');