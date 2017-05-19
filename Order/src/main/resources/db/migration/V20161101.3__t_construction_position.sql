CREATE TABLE `t_construction_position` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8  COMMENT='施工部位表';;

insert into t_construction_position values
('1','前风挡'),
('2','左前门'),
(3,'右前门'),
(4,'左后门+角'),
(5,'右后门+角'),
(6,'后风挡'),
(7,'前保险杠'),
(8,'引擎盖'),
(9,'左右前叶子板'),
(10,'四门'),
(11,'左右后叶子板'),
(12,'尾盖'),
(13,'后保险杠'),
(14,'ABC柱套件'),
(15,'车顶'),
(16,'门拉手'),
(17,'反光镜'),
(18,'整车');




