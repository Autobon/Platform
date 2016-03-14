CREATE TABLE `t_tech_stat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tech_id` int(11) DEFAULT NULL,
  `star_rate` float(5,2) DEFAULT '3.5' COMMENT '星级',
  `balance` float(10,2) DEFAULT '0.00' COMMENT '余额',
  `unpaid_orders` int(11) DEFAULT '0' COMMENT '未付账订单条数',
  `total_orders` int(11) DEFAULT '0' COMMENT '订单总条数',
  `comment_count` int(11) DEFAULT '0' COMMENT '评论条数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_t_tech_stat_tech_id` (`tech_id`)
) DEFAULT CHARSET=utf8;


