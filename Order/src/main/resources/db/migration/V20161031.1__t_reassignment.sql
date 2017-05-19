CREATE TABLE `t_reassignment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL  COMMENT '订单编号',
  `applicant` int(11) DEFAULT NULL COMMENT '申请人',
  `assigned_person` int(11) DEFAULT NULL  COMMENT '被指派人',
  `status` int(1) DEFAULT NULL COMMENT '状态 0 未指派 1已指派 ',
  `create_user` int(11) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT  '创建时间',
  `modify_user` int(11) DEFAULT NULL COMMENT '修改人',
  `modify_date` datetime DEFAULT NULL COMMENT  '修改时间',
  PRIMARY KEY (`id`)
)  DEFAULT CHARSET=utf8 COMMENT='申请改派表';
