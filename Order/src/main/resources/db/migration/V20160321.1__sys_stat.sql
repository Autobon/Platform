CREATE TABLE `sys_stat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `new_order_count` int(11) DEFAULT NULL  COMMENT '新增订单数',
  `finished_order_count` int(11) DEFAULT NULL COMMENT '完成订单数',
  `new_coop_count` int(11) DEFAULT NULL  COMMENT '新增商户数',
  `verified_coop_count` int(11) DEFAULT NULL COMMENT '认证商户数',
  `new_tech_count` int(11) DEFAULT NULL COMMENT '新增技师数',
  `verified_tech_count` int(11) DEFAULT NULL COMMENT '认证技师数',
  `stat_time` datetime DEFAULT NULL COMMENT  '统计日期',
  `stat_type` int(11) DEFAULT NULL COMMENT '统计类型(1-按天统计  2-按月统计)',
  PRIMARY KEY (`id`),
  INDEX idx_stat_time(stat_time),
  INDEX idx_stat_type(stat_type)
)  DEFAULT CHARSET=utf8 COMMENT='系统统计数据表';
