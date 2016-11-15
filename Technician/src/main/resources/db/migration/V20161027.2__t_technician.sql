alter table t_technician add reference VARCHAR(11) default null  COMMENT '推荐人号码（注册用户）';

alter table t_technician add resume VARCHAR(400) default null  COMMENT '个人说明 少于200个汉字';

alter table t_technician add film_level int(1) default 0  COMMENT '隔热膜星级';
alter table t_technician add car_cover_level   int(1) default 0  COMMENT '车衣星级';
alter table t_technician add color_modify_level int(1) default 0  COMMENT '改色星级';
alter table t_technician add beauty_level  int(1) default 0  COMMENT '美容星级';

alter table t_technician add film_working_seniority int(2) default 0  COMMENT '隔热膜年限';
alter table t_technician add car_cover_working_seniority   int(2) default 0  COMMENT '车衣年限';
alter table t_technician add color_modify_working_seniority int(2) default 0  COMMENT '改色年限';
alter table t_technician add beauty_working_seniority  int(2) default 0  COMMENT '美容年限';
