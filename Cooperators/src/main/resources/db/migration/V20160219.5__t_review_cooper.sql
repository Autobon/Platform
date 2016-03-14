CREATE TABLE `t_review_cooper` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cooperators_id` int(11) DEFAULT NULL COMMENT '合作商户id',
  `review_time` datetime DEFAULT NULL COMMENT '审核时间',
  `checked_by` varchar(30) DEFAULT NULL COMMENT '审核人',
  `result_desc` varchar(255) DEFAULT NULL COMMENT '审核失败原因描述',
  PRIMARY KEY (`id`)
)DEFAULT CHARSET=utf8 COMMENT='合作商户审核表';
