CREATE TABLE `t_role_function_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `category_id` VARCHAR(200) NOT NULL COMMENT '类别ID',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COMMENT='角色功能分类表';
