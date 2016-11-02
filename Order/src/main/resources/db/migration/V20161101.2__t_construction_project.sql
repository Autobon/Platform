CREATE TABLE `t_construction_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '项目名称',
  `ids` varchar(100) NOT NULL COMMENT '施工部位ID 逗号分隔',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8  COMMENT='施工项目表';;


insert into t_construction_project(name, ids) values ('隔热膜','1,2,3,4,5,6');
insert into t_construction_project(name, ids) values ('隐形车衣','7,8,9,10,11,12,13,14,15,16,17,18');
insert into t_construction_project(name, ids) values ('车身改色','18');
insert into t_construction_project(name, ids) values ('美容清洁','18');
