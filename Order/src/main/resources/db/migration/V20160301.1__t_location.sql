CREATE TABLE IF NOT EXISTS t_location (
  id int(11) NOT NULL,
  get_time datetime DEFAULT NULL COMMENT '同步位置时间',
  rtposition_lon varchar(20) DEFAULT NULL COMMENT '实时位置经度',
  rtposition_lat varchar(20) DEFAULT NULL COMMENT '实时位置纬度',
  technician_id int(11) DEFAULT NULL COMMENT '技师id',
  PRIMARY KEY (id)
)DEFAULT CHARSET=utf8 COMMENT='技师实时位置表';