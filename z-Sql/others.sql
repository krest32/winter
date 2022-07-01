

CREATE TABLE `others_banner` (
  `id` char(19) NOT NULL DEFAULT '' COMMENT 'ID',
  `title` varchar(20) DEFAULT '' COMMENT '标题',
  `image_filename` varchar(500) NOT NULL DEFAULT '' COMMENT '图片名称',
  `image_address` varchar(500) NOT NULL DEFAULT '' COMMENT '图片地址',
  `link_url` varchar(500) DEFAULT '' COMMENT '链接地址',
  `sort` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
  `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='首页banner表';


CREATE TABLE `others_mq_message` (
  `id` CHAR(19) NOT NULL DEFAULT '' COMMENT '消息ID',
  `content` TEXT COMMENT '消息内容',
  `to_exchage` VARCHAR(500) NOT NULL DEFAULT '' COMMENT '交换机',
  `routing_key` VARCHAR(500) NOT NULL DEFAULT '' COMMENT '路由',
  `class_type` VARCHAR(500) DEFAULT NULL ,
  `message_status` INT(1) NOT NULL DEFAULT '0' COMMENT '0 新建 1 发送 2 错误抵达 3 已经抵达',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='MQ消息状态表';

CREATE TABLE `t_order` (
  `id` char(19) NOT NULL DEFAULT ''COMMENT '订单号',
  `order_detail_id` varchar(100) DEFAULT NULL COMMENT '订单详细Id',
  `member_id` varchar(19) NOT NULL DEFAULT '' COMMENT '会员id',
  `nickname` varchar(50) DEFAULT NULL COMMENT '会员昵称',
  `mobile` varchar(11) DEFAULT NULL COMMENT '会员手机',
  `total_fee` decimal(10,2) DEFAULT '0.01' COMMENT '订单金额（分）',
  `pay_type` tinyint(3) DEFAULT NULL COMMENT '支付类型（1：微信 2：支付宝）',
  `status` tinyint(3) DEFAULT NULL COMMENT '订单状态（0：未支付 1：已支付 2: 超时取消）',
  `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `id_order_detail_id` (`order_detail_id`),
  KEY  `idx_member_id` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';


CREATE TABLE `product_order_detail` (
  `id` char(19) NOT NULL DEFAULT ''COMMENT '订单详情主键Id',
  `order_id` varchar(100) DEFAULT NULL COMMENT '订单Id',
  `product_id` varchar(19) NOT NULL DEFAULT '' COMMENT '产品id',
  `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY (`order_id`),
  KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';

CREATE TABLE `product_order_paylog` (
  `id` char(19) NOT NULL DEFAULT '' COMMENT '支付日志主键',
  `order_no` varchar(20) NOT NULL DEFAULT '' COMMENT '订单号',
  `pay_time` datetime DEFAULT NULL COMMENT '支付完成时间',
  `total_fee` decimal(10,2) DEFAULT '0.01' COMMENT '支付金额（分）',
  `transaction_id` varchar(30) DEFAULT NULL COMMENT '交易流水号',
  `trade_state` char(20) DEFAULT NULL COMMENT '交易状态',
  `pay_type` tinyint(3) NOT NULL DEFAULT '0' COMMENT '支付类型（1：微信 2：支付宝）',
  `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付日志表';


CREATE TABLE `stock_worklist` (
  `id` char(19) NOT NULL DEFAULT '' COMMENT '库存工作单',
  `order_id` char(19) NOT NULL DEFAULT '' COMMENT '订单id',
  `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存工作单';


CREATE TABLE `stock_worklist_detail` (
  `id` char(19) NOT NULL DEFAULT '' COMMENT '库存详情单id',
  `order_id` char(19) NOT NULL DEFAULT '' COMMENT '订单id',
  `product_id` char(19) NOT NULL DEFAULT '' COMMENT '产品id',
  `product_title` varchar(255) NOT NULL DEFAULT '' COMMENT '产品名称',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存详情单';