CREATE TABLE IF NOT EXISTS t_account (
  id int(11) NOT NULL AUTO_INCREMENT,
  phone varchar(11) DEFAULT NULL COMMENT '手机号码',
  password varchar(32) DEFAULT NULL COMMENT '密码',
  identify_salt varchar(4) DEFAULT NULL COMMENT 'salt',
  register_time DATETIME DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (id)
)  COMMENT='登陆账户表';
