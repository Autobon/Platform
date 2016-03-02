CREATE TABLE IF NOT EXISTS sys_codeitem (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `codemap` varchar(32) NOT NULL COMMENT '字典code',
  `code` varchar(32) NOT NULL COMMENT '字典选项code',
  `name` varchar(32) NOT NULL COMMENT '字典选项名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `ci_codemap_index` (`codemap`),
  KEY `ci_code_index` (`code`)
) COMMENT='字典item表';