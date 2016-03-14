CREATE TABLE IF NOT EXISTS t_comment (
  id int(11) NOT NULL AUTO_INCREMENT,
  tech_id int(11) DEFAULT NULL COMMENT '技师id',
  order_id int(11) DEFAULT NULL COMMENT '订单id',
  star int(2) DEFAULT '0' COMMENT '星数(1,2,3,4,5)',
  arrive_on_time tinyint(1) DEFAULT FALSE COMMENT '准时到达(1,选择0,未选择)',
  complete_on_time tinyint(1) DEFAULT FALSE COMMENT '准时完工',
  professional tinyint(1) DEFAULT FALSE COMMENT '技术专业',
  dress_neatly tinyint(1) DEFAULT FALSE COMMENT '着装整洁',
  car_protect tinyint(1) DEFAULT FALSE COMMENT '车辆保护超级棒',
  good_attitude tinyint(1) DEFAULT FALSE COMMENT '好态度',
  advice varchar(255)  DEFAULT NULL COMMENT '建议',
  create_at DATETIME DEFAULT NULL,
  PRIMARY KEY (id)
)DEFAULT CHARSET=utf8 COMMENT='评论表';