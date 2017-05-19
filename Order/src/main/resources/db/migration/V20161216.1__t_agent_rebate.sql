CREATE TABLE `t_agent_rebate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `primary_agent` int(2) DEFAULT NULL COMMENT '一级代理',
  `second_agent` int(2) DEFAULT NULL COMMENT '二级代理',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8  COMMENT='回扣提成';
