CREATE TABLE IF NOT EXISTS t_coop_account (
  id int(11) NOT NULL AUTO_INCREMENT,
  cooperator_id int(11) DEFAULT NULL COMMENT '合作商户id',
  is_main tinyint(1) DEFAULT FALSE COMMENT '是否主账户(0,不是,1是)',
  fired tinyint(1) DEFAULT FALSE COMMENT '是否离职(0,不是,1是)',
  phone varchar(20) NOT NULL DEFAULT '' COMMENT '手机号',
  name varchar(20) NOT NULL DEFAULT '' COMMENT '姓名',
  gender tinyint(1) COMMENT '性别(0,男，1，女)',
  password varchar(255) DEFAULT NULL COMMENT '密码',
  last_login_time datetime DEFAULT NULL COMMENT '上次登录时间',
  last_login_ip varchar(20) DEFAULT NULL COMMENT '上次登录IP',
  create_time datetime DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (id)
)DEFAULT CHARSET=utf8 COMMENT='合作商户业务员表';