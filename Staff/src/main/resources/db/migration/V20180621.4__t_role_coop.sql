CREATE TABLE `t_role_coop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT 0 COMMENT '角色ID',
  `coop_id` int(11) DEFAULT 0 COMMENT '商户ID',
  PRIMARY KEY (`id`)
)DEFAULT CHARSET=utf8  COMMENT='角色商户关系表';


