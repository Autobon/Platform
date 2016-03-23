CREATE TABLE `t_cooperators` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(20) NOT NULL DEFAULT '' COMMENT '账号',
  `shortname` varchar(20) DEFAULT NULL COMMENT '企业简称',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `fullname` varchar(255) DEFAULT NULL COMMENT '企业名称',
  `business_license` varchar(30) DEFAULT NULL COMMENT '营业执照号',
  `corporation_name` varchar(20) DEFAULT NULL COMMENT '法人姓名',
  `corporation_id_no` varchar(20) DEFAULT NULL COMMENT '法人身份证号',
  `bussiness_license_pic` varchar(255) DEFAULT NULL COMMENT '营业执照副本照片',
  `corporation_id_pic_a` varchar(255) DEFAULT NULL COMMENT '法人身份证正面照',
  `corporation_id_pic_b` varchar(255) DEFAULT NULL COMMENT '法人身份证背面照',
  `longitude` varchar(20) DEFAULT NULL COMMENT '商户位置经度',
  `latitude` varchar(20) DEFAULT NULL COMMENT '商户位置纬度',
  `invoice_header` varchar(100) DEFAULT NULL COMMENT '发票抬头',
  `tax_id_no` varchar(30) DEFAULT NULL COMMENT '纳税识别号',
  `postcode` varchar(10) DEFAULT NULL COMMENT '邮政编码',
  `province` varchar(20) DEFAULT NULL COMMENT '省',
  `city` varchar(20) DEFAULT NULL COMMENT '市',
  `district` varchar(30) DEFAULT NULL COMMENT '区',
  `address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `contact` varchar(20) DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系人电话',
  `status_code` int(1) DEFAULT NULL COMMENT '状态 0-未审核 1-审核成功 2-审核失败 3-账号禁用',
  `last_login_time` datetime DEFAULT NULL COMMENT '上次登录时间',
  `last_login_ip` varchar(20) DEFAULT NULL COMMENT '上次登录IP',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `is_main` tinyint(1) DEFAULT FALSE COMMENT '是否主账户(0,不是,1是)',
  `push_id` VARCHAR(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_coop_phone` (`phone`),
  UNIQUE KEY `uniq_coop_shortname` (`shortname`),
  KEY `tech_push_id` (`push_id`)
)DEFAULT CHARSET=utf8 COMMENT='合作商户表';

insert into `t_cooperators` (`id`, `phone`, `shortname`, `password`, `fullname`, `business_license`, `corporation_name`, `corporation_id_no`, `bussiness_license_pic`, `corporation_id_pic_a`, `corporation_id_pic_b`, `longitude`, `latitude`, `invoice_header`, `tax_id_no`, `postcode`, `province`, `city`, `district`, `address`, `contact`, `contact_phone`, `status_code`, `last_login_time`, `last_login_ip`, `create_time`,is_main,push_id) values('1','13072705000','first-coop','7c4a8d09ca3762af61e59520943dc26494f8941b',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'13072705000','1',NULL,NULL,'2016-03-11 14:18:53',true,NULL );