alter table t_order add  vehicle_model varchar(20) DEFAULT NULL   COMMENT '车型';
alter table t_order add  real_order_num  varchar(20) DEFAULT NULL COMMENT '真实订单号';

alter table t_order add  license varchar(10) DEFAULT NULL   COMMENT '车牌号';
alter table t_order add  vin  varchar(60) DEFAULT NULL COMMENT '车架号';


update t_order set type = '1,2' where id = 1 or id =2 ;