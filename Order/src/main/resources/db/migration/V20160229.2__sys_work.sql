CREATE TABLE IF NOT EXISTS sys_work (
  id int(11) NOT NULL,
  order_type int(2) NOT NULL COMMENT '订单类型(1-隔热膜 2-隐形车衣 3-车身改色 4-美容清洁)',
  order_type_name varchar(32) NOT NULL COMMENT '订单类型名称',
  car_seat int(2) DEFAULT 0 COMMENT '5座，7座，没有则为0',
  work_name varchar(32) NOT NULL COMMENT '工作项',
  price int(10) NOT NULL COMMENT '价格',
  PRIMARY KEY (id)
)DEFAULT CHARSET=utf8 COMMENT='工作项表';