CREATE TABLE IF NOT EXISTS sys_work_item (
  id int(11) NOT NULL,
  order_type int(2) NOT NULL COMMENT '订单类型(1-隔热膜 2-隐形车衣 3-车身改色 4-美容清洁)',
  order_type_name varchar(32) NOT NULL COMMENT '订单类型名称',
  car_seat int(2) DEFAULT 0 COMMENT '5座，7座，没有则为0',
  work_name varchar(32) NOT NULL COMMENT '工作项',
  price float(5,2) NOT NULL COMMENT '价格',
  PRIMARY KEY (id)
)DEFAULT CHARSET=utf8 COMMENT='工作项表';

INSERT INTO `sys_work_item` VALUES ('1', '1', '隔热层', '5', '前风挡', '100');
INSERT INTO `sys_work_item` VALUES ('2', '1', '隔热层', '5', '左前门', '100');
INSERT INTO `sys_work_item` VALUES ('3', '1', '隔热层', '5', '右前门', '100');
INSERT INTO `sys_work_item` VALUES ('4', '1', '隔热层', '5', '左后门', '100');
INSERT INTO `sys_work_item` VALUES ('5', '1', '隔热层', '5', '右后门', '100');
INSERT INTO `sys_work_item` VALUES ('6', '1', '隔热层', '5', '左大角', '100');
INSERT INTO `sys_work_item` VALUES ('7', '1', '隔热层', '5', '右大角', '100');
INSERT INTO `sys_work_item` VALUES ('8', '1', '隔热层', '5', '后风档', '100');
INSERT INTO `sys_work_item` VALUES ('9', '1', '隔热层', '7', '前风挡', '100');
INSERT INTO `sys_work_item` VALUES ('10', '1', '隔热层', '7', '左前门', '100');
INSERT INTO `sys_work_item` VALUES ('11', '1', '隔热层', '7', '右前门', '100');
INSERT INTO `sys_work_item` VALUES ('12', '1', '隔热层', '7', '左中门', '100');
INSERT INTO `sys_work_item` VALUES ('13', '1', '隔热层', '7', '右中门', '100');
INSERT INTO `sys_work_item` VALUES ('14', '1', '隔热层', '7', '左后门', '100');
INSERT INTO `sys_work_item` VALUES ('15', '1', '隔热层', '7', '右后门', '100');
INSERT INTO `sys_work_item` VALUES ('16', '1', '隔热层', '7', '左大角', '100');
INSERT INTO `sys_work_item` VALUES ('17', '1', '隔热层', '7', '右大角', '100');
INSERT INTO `sys_work_item` VALUES ('18', '1', '隔热层', '7', '后风档', '100');
INSERT INTO `sys_work_item` VALUES ('19', '2', '隐形车衣', '5', '前保险杠', '100');
INSERT INTO `sys_work_item` VALUES ('20', '2', '隐形车衣', '5', '引擎盖', '100');
INSERT INTO `sys_work_item` VALUES ('21', '2', '隐形车衣', '5', '左前翼子板', '100');
INSERT INTO `sys_work_item` VALUES ('22', '2', '隐形车衣', '5', '右前翼子板', '100');
INSERT INTO `sys_work_item` VALUES ('23', '2', '隐形车衣', '5', '左前门', '100');
INSERT INTO `sys_work_item` VALUES ('24', '2', '隐形车衣', '5', '右前门', '100');
INSERT INTO `sys_work_item` VALUES ('25', '2', '隐形车衣', '5', '左后门', '100');
INSERT INTO `sys_work_item` VALUES ('26', '2', '隐形车衣', '5', '右后门', '100');
INSERT INTO `sys_work_item` VALUES ('27', '2', '隐形车衣', '5', '左后翼子板', '100');
INSERT INTO `sys_work_item` VALUES ('28', '2', '隐形车衣', '5', '右后翼子板', '100');
INSERT INTO `sys_work_item` VALUES ('29', '2', '隐形车衣', '5', '左底边', '100');
INSERT INTO `sys_work_item` VALUES ('30', '2', '隐形车衣', '5', '右底边', '100');
INSERT INTO `sys_work_item` VALUES ('31', '2', '隐形车衣', '5', '后备箱盖', '100');
INSERT INTO `sys_work_item` VALUES ('32', '2', '隐形车衣', '5', '后保险杠', '100');
INSERT INTO `sys_work_item` VALUES ('33', '2', '隐形车衣', '5', '车顶', '100');
INSERT INTO `sys_work_item` VALUES ('34', '2', '隐形车衣', '5', '左后视镜', '100');
INSERT INTO `sys_work_item` VALUES ('35', '2', '隐形车衣', '5', '右后视镜', '100');
INSERT INTO `sys_work_item` VALUES ('36', '2', '隐形车衣', '7', '前保险杠', '100');
INSERT INTO `sys_work_item` VALUES ('37', '2', '隐形车衣', '7', '引擎盖', '100');
INSERT INTO `sys_work_item` VALUES ('38', '2', '隐形车衣', '7', '左前翼子板', '100');
INSERT INTO `sys_work_item` VALUES ('39', '2', '隐形车衣', '7', '右前翼子板', '100');
INSERT INTO `sys_work_item` VALUES ('40', '2', '隐形车衣', '7', '左前门', '100');
INSERT INTO `sys_work_item` VALUES ('41', '2', '隐形车衣', '7', '右前门', '100');
INSERT INTO `sys_work_item` VALUES ('42', '2', '隐形车衣', '7', '左后门', '100');
INSERT INTO `sys_work_item` VALUES ('43', '2', '隐形车衣', '7', '右后门', '100');
INSERT INTO `sys_work_item` VALUES ('44', '2', '隐形车衣', '7', '左中门', '100');
INSERT INTO `sys_work_item` VALUES ('45', '2', '隐形车衣', '7', '右中门', '100');
INSERT INTO `sys_work_item` VALUES ('46', '2', '隐形车衣', '7', '左后翼子板', '100');
INSERT INTO `sys_work_item` VALUES ('47', '2', '隐形车衣', '7', '右后翼子板', '100');
INSERT INTO `sys_work_item` VALUES ('48', '2', '隐形车衣', '7', '左底边', '100');
INSERT INTO `sys_work_item` VALUES ('49', '2', '隐形车衣', '7', '右底边', '100');
INSERT INTO `sys_work_item` VALUES ('50', '2', '隐形车衣', '7', '后备箱盖', '100');
INSERT INTO `sys_work_item` VALUES ('51', '2', '隐形车衣', '7', '后保险杠', '100');
INSERT INTO `sys_work_item` VALUES ('52', '2', '隐形车衣', '7', '车顶', '100');
INSERT INTO `sys_work_item` VALUES ('53', '2', '隐形车衣', '7', '左后视镜', '100');
INSERT INTO `sys_work_item` VALUES ('54', '2', '隐形车衣', '7', '右后视镜', '100');
INSERT INTO `sys_work_item` VALUES ('55', '3', '车身改色', '5', '前保险杠', '100');
INSERT INTO `sys_work_item` VALUES ('56', '3', '车身改色', '5', '引擎盖', '100');
INSERT INTO `sys_work_item` VALUES ('57', '3', '车身改色', '5', '左前翼子板', '100');
INSERT INTO `sys_work_item` VALUES ('58', '3', '车身改色', '5', '右前翼子板', '100');
INSERT INTO `sys_work_item` VALUES ('59', '3', '车身改色', '5', '左前门', '100');
INSERT INTO `sys_work_item` VALUES ('60', '3', '车身改色', '5', '右前门', '100');
INSERT INTO `sys_work_item` VALUES ('61', '3', '车身改色', '5', '左后门', '100');
INSERT INTO `sys_work_item` VALUES ('62', '3', '车身改色', '5', '右后门', '100');
INSERT INTO `sys_work_item` VALUES ('63', '3', '车身改色', '5', '左后翼子板', '100');
INSERT INTO `sys_work_item` VALUES ('64', '3', '车身改色', '5', '右后翼子板', '100');
INSERT INTO `sys_work_item` VALUES ('65', '3', '车身改色', '5', '左底边', '100');
INSERT INTO `sys_work_item` VALUES ('66', '3', '车身改色', '5', '右底边', '100');
INSERT INTO `sys_work_item` VALUES ('67', '3', '车身改色', '5', '后备箱盖', '100');
INSERT INTO `sys_work_item` VALUES ('68', '3', '车身改色', '5', '后保险杠', '100');
INSERT INTO `sys_work_item` VALUES ('69', '3', '车身改色', '5', '车顶', '100');
INSERT INTO `sys_work_item` VALUES ('70', '3', '车身改色', '5', '左后视镜', '100');
INSERT INTO `sys_work_item` VALUES ('71', '3', '车身改色', '5', '右后视镜', '100');
INSERT INTO `sys_work_item` VALUES ('72', '3', '车身改色', '7', '前保险杠', '100');
INSERT INTO `sys_work_item` VALUES ('73', '3', '车身改色', '7', '引擎盖', '100');
INSERT INTO `sys_work_item` VALUES ('74', '3', '车身改色', '7', '左前翼子板', '100');
INSERT INTO `sys_work_item` VALUES ('75', '3', '车身改色', '7', '右前翼子板', '100');
INSERT INTO `sys_work_item` VALUES ('76', '3', '车身改色', '7', '左前门', '100');
INSERT INTO `sys_work_item` VALUES ('77', '3', '车身改色', '7', '右前门', '100');
INSERT INTO `sys_work_item` VALUES ('78', '3', '车身改色', '7', '左后门', '100');
INSERT INTO `sys_work_item` VALUES ('79', '3', '车身改色', '7', '右后门', '100');
INSERT INTO `sys_work_item` VALUES ('80', '3', '车身改色', '7', '左中门', '100');
INSERT INTO `sys_work_item` VALUES ('81', '3', '车身改色', '7', '右中门', '100');
INSERT INTO `sys_work_item` VALUES ('82', '3', '车身改色', '7', '左后翼子板', '100');
INSERT INTO `sys_work_item` VALUES ('83', '3', '车身改色', '7', '右后翼子板', '100');
INSERT INTO `sys_work_item` VALUES ('84', '3', '车身改色', '7', '左底边', '100');
INSERT INTO `sys_work_item` VALUES ('85', '3', '车身改色', '7', '右底边', '100');
INSERT INTO `sys_work_item` VALUES ('86', '3', '车身改色', '7', '后备箱盖', '100');
INSERT INTO `sys_work_item` VALUES ('87', '3', '车身改色', '7', '后保险杠', '100');
INSERT INTO `sys_work_item` VALUES ('88', '3', '车身改色', '7', '车顶', '100');
INSERT INTO `sys_work_item` VALUES ('89', '3', '车身改色', '7', '左后视镜', '100');
INSERT INTO `sys_work_item` VALUES ('90', '3', '车身改色', '7', '右后视镜', '100');
INSERT INTO `sys_work_item` VALUES ('91', '4', '美容清洁', '0', '百分比', '100');
