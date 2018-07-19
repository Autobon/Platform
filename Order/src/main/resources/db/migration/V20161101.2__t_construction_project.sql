CREATE TABLE `t_construction_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '项目名称',
  `ids` varchar(100) NOT NULL COMMENT '施工部位ID 逗号分隔',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8  COMMENT='施工项目表';;


insert into t_construction_project(name, ids) values ('隔热膜','1,79,6,2,3,4,5,22,23,24,25,80,81,82');
insert into t_construction_project(name, ids) values ('隐形车衣','18,28,15,8,2,4,31,33,3,5,32,34,26,35,29,30,27,36,12,43,44,45,46,37,42,41,39,40,38');
insert into t_construction_project(name, ids) values ('车身改色','18,28,15,8,2,4,31,33,3,5,32,34,26,35,29,30,27,36,12,43,44,45,46,37,42,41,39,40,38');
insert into t_construction_project(name, ids) values ('美容清洁','83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105');
