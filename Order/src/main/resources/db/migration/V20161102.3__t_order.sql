alter table t_order add  before_photos varchar(1024) DEFAULT NULL COMMENT '施工前照片数组,用逗号隔开';
alter table t_order add  after_photos varchar(2048) DEFAULT NULL COMMENT '施工后照片数组,用逗号隔开';
alter table t_order add   start_time datetime DEFAULT NULL COMMENT '施工开始时间';
alter table t_order add   end_time datetime DEFAULT NULL COMMENT '施工结束时间';
alter table t_order add   sign_time datetime DEFAULT NULL COMMENT '签到时间';