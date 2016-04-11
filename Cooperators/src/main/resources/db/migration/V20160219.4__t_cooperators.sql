CREATE TABLE `t_cooperators` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
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
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  status_code int(1) DEFAULT NULL COMMENT '状态 0-未审核 1-审核成功 2-审核失败 ',
  order_num int(10) DEFAULT 0 COMMENT '商户订单数',
  PRIMARY KEY (`id`)
)DEFAULT CHARSET=utf8 COMMENT='合作商户表';

insert into `t_cooperators` (`id`, `fullname`, `business_license`, `corporation_name`, `corporation_id_no`, `bussiness_license_pic`, `corporation_id_pic_a`, `corporation_id_pic_b`, `longitude`, `latitude`, `invoice_header`, `tax_id_no`, `postcode`, `province`, `city`, `district`, `address`, `contact`, `contact_phone`, `create_time`,status_code,order_num) values('1','非常历害的公司',NULL,'王大拿',NULL,NULL,NULL,NULL,'114.287685','30.639203',NULL,NULL,NULL,'湖北省','武汉市','洪山区','软件园中路','王大拿','13072705000','2016-03-11 14:18:53','1',2);
