CREATE TABLE `t_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(30) DEFAULT NULL COMMENT '标题',
  `content` varchar(255) DEFAULT NULL COMMENT '消息内容',
  `sendto` int(1) DEFAULT NULL COMMENT '发送给1技师端2合作商户',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` int(1) DEFAULT NULL COMMENT '状态1-已发布0-未发布',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息通知';

INSERT INTO `t_message` (`title`,`content`,`sendto`,`update_time`,`status`) VALUES ('测试','发送给技师',1,'2016-04-03 19:59:00',0);
INSERT INTO `t_message` (`title`,`content`,`sendto`,`update_time`,`status`) VALUES ('测试','发送给合作商户',2,'2016-04-03 18:59:00',1);