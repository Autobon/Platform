CREATE TABLE `t_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT 'URL',
  `remark` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='菜单表';


insert into t_menu values(1, '首页', 'console.home', '首页');
insert into t_menu values(2, '订单管理', 'console.order', '订单管理');
insert into t_menu values(3, '商户管理', 'console.cooperator', '商户管理');
insert into t_menu values(4, '技师管理', 'console.technician', '技师管理');
insert into t_menu values(5, '财务管理', 'console.finance', '财务管理');
insert into t_menu values(6, '产品管理', 'console.product', '产品管理');
insert into t_menu values(7, '统计分析', 'console.stat', '统计分析');
insert into t_menu values(8, '学习园地', 'console.study', '学习园地');
insert into t_menu values(9, '用户管理', 'console.staff', '用户管理');
insert into t_menu values(10, '角色管理', 'console.role', '角色管理');
insert into t_menu values(11, '团队管理', 'console.team', '团队管理');
insert into t_menu values(12, '跟单员管理', 'console.merchandiser', '跟单员管理');