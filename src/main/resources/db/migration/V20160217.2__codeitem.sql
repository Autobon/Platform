CREATE TABLE IF NOT EXISTS sys_codeitem (
  id int(11) NOT NULL,
  code varchar(32) NOT NULL COMMENT '字典选项code',
  codemap varchar(32) NOT NULL COMMENT '字典code',
  name varchar(32) NOT NULL COMMENT '字典选项名称',
  remark varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id)
) COMMENT='字典item表';