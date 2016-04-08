CREATE TABLE IF NOT EXISTS t_location (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_at datetime DEFAULT NULL COMMENT '同步位置时间',
  lng varchar(20) DEFAULT NULL COMMENT '实时位置经度',
  lat varchar(20) DEFAULT NULL COMMENT '实时位置纬度',
  province varchar(20) DEFAULT NULL COMMENT '省',
  city varchar(20) DEFAULT NULL COMMENT '市',
  district varchar(20) DEFAULT NULL COMMENT '区县',
  street varchar(20) DEFAULT NULL COMMENT '街道',
  street_number varchar(20) DEFAULT NULL COMMENT '街道号',
  tech_id int(11) DEFAULT NULL COMMENT '技师id',
  PRIMARY KEY (id),
  KEY `province` (`province`),
  KEY `city` (`city`),
  KEY `prov_city` (`province`, `city`),
  KEY `prov_city_dist` (`province`, `city`, `district`)
)DEFAULT CHARSET=utf8 COMMENT='技师实时位置表';
