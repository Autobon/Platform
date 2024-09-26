alter table t_order add technician_remark VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '技师备注';

alter table t_order add make_up_remark VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '回填备注';