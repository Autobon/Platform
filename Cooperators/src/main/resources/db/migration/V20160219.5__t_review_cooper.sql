CREATE TABLE `t_review_cooper` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cooperator_id` int(11) DEFAULT NULL COMMENT '合作商户id',
  `review_time` datetime DEFAULT NULL COMMENT '审核时间',
  `reviewer_id` int(11) DEFAULT NULL COMMENT '审核人ID',
  `remark` varchar(255) DEFAULT NULL COMMENT '审核失败原因描述',
  result tinyint(1) DEFAULT TRUE  COMMENT '审核是否成功,默认成功true',
  PRIMARY KEY (`id`)
)DEFAULT CHARSET=utf8 COMMENT='合作商户审核表';
