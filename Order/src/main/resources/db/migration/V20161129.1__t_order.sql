alter table t_order add  product_status int(1) DEFAULT '0' COMMENT '订单补录产品状态 0 未补录,1已补录';
alter table t_order add  reassignment_status  int(1) DEFAULT '0' COMMENT '申请改派状态 0 未申请改派,1已申请改派 2已处理';

alter table t_order modify column photo varchar(1024);