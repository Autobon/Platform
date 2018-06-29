CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
   function_category_ids varchar(500) DEFAULT NULL COMMENT '角色权限:t_function_category中id的队列,逗号分隔',
   coop_ids varchar(500) DEFAULT NULL COMMENT '商户ID,逗号分隔',
   menu_ids  varchar(500) DEFAULT NULL COMMENT '菜单ID,逗号分隔',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='角色表';


insert into t_role values(1, '超级管理员', '超级管理员', '1,2', '1,2', '1,2,3,4,5,6,7,8,9,10,11,12,13,14');