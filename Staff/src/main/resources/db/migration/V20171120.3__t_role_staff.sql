CREATE TABLE `t_role_staff` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(50) DEFAULT 0 COMMENT '角色ID',
  `staff_id` int(50) DEFAULT 0 COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='角色用户关系表';

insert into t_role_menu values(1, 1, 1);