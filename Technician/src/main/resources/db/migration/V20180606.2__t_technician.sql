alter table t_technician add remark VARCHAR(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注';

alter table t_technician add team_id int(11) DEFAULT NULL COMMENT '团队ID';