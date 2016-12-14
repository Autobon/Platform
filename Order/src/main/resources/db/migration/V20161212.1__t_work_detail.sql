 alter table t_work_detail add  order_num  varchar(20) DEFAULT NULL COMMENT '订单编号';
 alter table t_work_detail add  source  int(1) DEFAULT '0' COMMENT '0 个人施工， 1下线回扣';

