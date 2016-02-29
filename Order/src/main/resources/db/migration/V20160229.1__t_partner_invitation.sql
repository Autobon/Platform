CREATE TABLE `t_partner_invitation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL,
  `main_tech_id` int(11) DEFAULT NULL,
  `invited_tech_id` int(11) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_at` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0：尚未接受邀约，1：已接受邀约，2：已拒绝邀约, 3: 已失效',
  PRIMARY KEY (`id`),
  KEY `invitation_order_index` (`order_id`),
  KEY `invitation_main_tech_index` (`main_tech_id`),
  KEY `invitation_invited_tech_index` (`invited_tech_id`),
  KEY `invitation_create_at_index` (`create_at`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
