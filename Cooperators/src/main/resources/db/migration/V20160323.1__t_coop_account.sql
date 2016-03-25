CREATE TABLE IF NOT EXISTS t_coop_account (
  id int(11) NOT NULL AUTO_INCREMENT,
  cooperator_id int(11) DEFAULT NULL COMMENT '合作商户id',
  is_main tinyint(1) DEFAULT FALSE COMMENT '是否主账户(0,不是,1是)',
  fired tinyint(1) DEFAULT FALSE COMMENT '是否离职(0,不是,1是)',
  shortname varchar(20) DEFAULT NULL COMMENT '企业简称',
  phone varchar(20) NOT NULL DEFAULT '' COMMENT '手机号',
  name varchar(20) DEFAULT '' COMMENT '姓名',
  gender tinyint(1) COMMENT '性别(0,男，1，女)',
  password varchar(255) DEFAULT NULL COMMENT '密码',
  last_login_time datetime DEFAULT NULL COMMENT '上次登录时间',
  last_login_ip varchar(20) DEFAULT NULL COMMENT '上次登录IP',
  create_time datetime DEFAULT NULL COMMENT '注册时间',
  push_id VARCHAR(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (id),
  KEY coop_push_id (push_id),
  UNIQUE KEY uniq_coop_shortname (shortname)
)DEFAULT CHARSET=utf8 COMMENT='合作商户账户表';

insert  into `t_coop_account`(`id`,`cooperator_id`,`is_main`,`fired`,`phone`,`name`,`gender`,`password`,`last_login_time`,`last_login_ip`,`create_time`,push_id) values (1,1,1,0,'13072726003','Tom',1,'7c4a8d09ca3762af61e59520943dc26494f8941b','2015-03-05 11:11:11','192.168.1.1','2015-03-02 11:11:11',NULL);