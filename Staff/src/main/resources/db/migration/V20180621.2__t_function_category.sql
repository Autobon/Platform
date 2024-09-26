CREATE TABLE `t_function_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(50) NOT NULL COMMENT '类别名称',
  `menu_id` int(11) DEFAULT NULL COMMENT '菜单ID',
  `is_default` int(1) DEFAULT NULL COMMENT '是否默认  0非默认 1默认',
   `is_enable` int(1) DEFAULT '0' COMMENT '是否可用 0 可用 1不可用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `category` (`category`)
) DEFAULT CHARSET=utf8 COMMENT='权限分类表';


INSERT INTO `t_function_category` VALUES (1, '首页查询', 1, 0, 0);
INSERT INTO `t_function_category` VALUES (2, '订单管理查询', 2, 0, 0);
INSERT INTO `t_function_category` VALUES (3, '订单管理编辑', 2, 0, 0);
INSERT INTO `t_function_category` VALUES (4, '商户管理查询', 3, 0, 0);
INSERT INTO `t_function_category` VALUES (5, '商户管理编辑', 3, 0, 0);
INSERT INTO `t_function_category` VALUES (6, '技师管理查询', 4, 0, 0);
INSERT INTO `t_function_category` VALUES (7, '技师管理编辑', 4, 0, 0);
INSERT INTO `t_function_category` VALUES (8, '财务管理查询', 5, 0, 0);
INSERT INTO `t_function_category` VALUES (9, '财务管理编辑', 5, 0, 0);
INSERT INTO `t_function_category` VALUES (10, '产品管理查询', 6, 0, 0);
INSERT INTO `t_function_category` VALUES (11, '产品管理编辑', 6, 0, 0);
INSERT INTO `t_function_category` VALUES (12, '统计分析查询', 7, 0, 0);
INSERT INTO `t_function_category` VALUES (13, '学习园地查询', 8, 0, 0);
INSERT INTO `t_function_category` VALUES (14, '学习园地编辑', 8, 0, 0);
INSERT INTO `t_function_category` VALUES (15, '用户管理查询', 9, 0, 0);
INSERT INTO `t_function_category` VALUES (16, '用户管理编辑', 9, 0, 0);
INSERT INTO `t_function_category` VALUES (17, '角色管理查询', 10, 0, 0);
INSERT INTO `t_function_category` VALUES (18, '角色管理编辑', 10, 0, 0);
INSERT INTO `t_function_category` VALUES (19, '团队管理查询', 11, 0, 0);
INSERT INTO `t_function_category` VALUES (20, '团队管理编辑', 11, 0, 0);
INSERT INTO `t_function_category` VALUES (21, '跟单员管理查询', 12, 0, 0);
INSERT INTO `t_function_category` VALUES (22, '跟单员管理编辑', 12, 0, 0);