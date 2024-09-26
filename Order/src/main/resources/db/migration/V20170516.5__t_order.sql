alter table t_order add  customer_name  varchar(20) DEFAULT NULL   COMMENT '客户名称';
alter table t_order add  customer_phone   varchar(15) DEFAULT NULL COMMENT '客户手机';

alter table t_order add  turnover  decimal DEFAULT 0   COMMENT '营业额';
alter table t_order add  salesman  varchar(10) DEFAULT NULL COMMENT '业务员';

