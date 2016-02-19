CREATE TABLE IF NOT EXISTS  sys_codemap (
  id int(11) NOT NULL,
  code varchar(32) NOT NULL COMMENT '字典code',
  name varchar(32) NOT NULL COMMENT '字典名称',
  PRIMARY KEY (id),
  UNIQUE KEY (code)
) COMMENT='字典map表';