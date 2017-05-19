 CREATE TABLE IF NOT EXISTS t_favorite_cooperator (
   id int(11) NOT NULL AUTO_INCREMENT,
   technician_id int(11) DEFAULT 0 COMMENT '技师id',
   cooperator_id int(11) DEFAULT 0 COMMENT '商户id',
   create_time datetime DEFAULT NULL COMMENT '注册时间',
   PRIMARY KEY (id)
 )DEFAULT CHARSET=utf8 COMMENT='收藏商户表';
