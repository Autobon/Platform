CREATE TABLE IF NOT EXISTS sys_codeitem (
  id int(11) NOT NULL,
  code varchar(32) NOT NULL,
  codemap varchar(32) NOT NULL,
  name varchar(32) NOT NULL,
  remark varchar(100) DEFAULT NULL,
  PRIMARY KEY (id)
) COMMENT='字典item表';