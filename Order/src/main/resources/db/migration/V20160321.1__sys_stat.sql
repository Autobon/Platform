CREATE TABLE `sys_stat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_count` int(11) DEFAULT NULL  COMMENT '新增订单数',
  `coop_count` int(11) DEFAULT NULL  COMMENT '新增商户数',
  `tech_count` int(11) DEFAULT NULL COMMENT '新增技师数',
  `stat_time` datetime DEFAULT NULL COMMENT  '统计日期',
  `stat_type` varchar(50) DEFAULT NULL COMMENT '统计类型(day-按天统计  month-按月统计)',
  PRIMARY KEY (`id`),
  INDEX idx_create_at(create_at),
  INDEX idx_stat_type(stat_type)
)  DEFAULT CHARSET=utf8 COMMENT='系统统计数据表';
