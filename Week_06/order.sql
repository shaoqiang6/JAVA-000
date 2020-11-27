drop table if EXISTS t_order;

CREATE TABLE `t_order` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '订单ID',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `status` smallint(4) NOT NULL DEFAULT '0' COMMENT '订单状态',
  `goods_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品总金额',
  `payment_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '支付方式',
  `freight_fee` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '运费',
  `paid_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '支付金额',
  `pay_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '支付状态',
  `c_t` int(11) NOT NULL DEFAULT '0',
  `u_t` int(11) NOT NULL DEFAULT '0',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `i_user_ct` (`user_id`,`c_t`) USING BTREE,
  KEY `i_ct` (`c_t`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if EXISTS t_order_item;

CREATE TABLE `t_order_item` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'oiID',
  `order_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '订单ID',
  `sku_id` int(11) NOT NULL DEFAULT '0',
  `goods_snapshot_id` int(11) NOT NULL DEFAULT '0' COMMENT '商品快照ID',
  `num` int(11) NOT NULL DEFAULT '0' COMMENT '下单数量',
   `coupon_discount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '优惠券优惠金额',
  `c_t` int(11) NOT NULL DEFAULT '0',
  `u_t` int(11) NOT NULL DEFAULT '0',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `i_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



