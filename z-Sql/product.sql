/* shop分类表 */
CREATE TABLE `product_cateLog` (
  `id` CHAR(20) NOT NULL COMMENT '分类主键ID',
  `title` CHAR(200) DEFAULT NULL COMMENT '分类名称',
  `parent_id` CHAR(20) NOT NULL COMMENT '父类ID,一级分类的付类Id为0',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  key (`parent_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='三级分类关系表';


/* 商品属性表 */
CREATE TABLE `product_attr` (
  `id` CHAR(20) NOT NULL COMMENT 'ID',
  `product_id` CHAR(20) NOT NULL COMMENT '商品ID',
  `attr_group_id` CHAR(20) NOT NULL COMMENT '属性分组Id',
  `attr_id` CHAR(20) NOT NULL COMMENT '属性Id',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY (`attr_id`),
  KEY (`product_id`),
  KEY (`attr_group_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='商品属性描述'


CREATE TABLE `product_brand` (
  `id` CHAR(20) NOT NULL COMMENT 'brand主键ID',
  `title` CHAR(20) DEFAULT NULL COMMENT '名称',
  `intro` CHAR(100) DEFAULT NULL COMMENT '介绍',
  `brand_image` CHAR(150) DEFAULT NULL COMMENT '属性图片',
  `brand_image_filename` CHAR(150) DEFAULT NULL COMMENT '属性图片名称',
  `one_sort_id` CHAR(20) NOT NULL COMMENT '一级分类Id',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY (`one_sort_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='品牌描述'


CREATE TABLE `product_brand_sort` (
  `id` CHAR(20) NOT NULL COMMENT '主键ID',
  `one_sort_id` CHAR(20) NOT NULL COMMENT  '一级分类的Id',
  `brand_id` CHAR(100) DEFAULT NULL COMMENT 'brand的Id',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY (`one_sort_id`),
  KEY (`brand_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='品牌分类关系表'


/* 商品信息表 */
CREATE TABLE `product` (
  `id` CHAR(20) NOT NULL COMMENT '商品Id',
  `title` CHAR(200) DEFAULT NULL COMMENT '商品名称',
  `brand_id` CHAR(20) NOT NULL COMMENT '品牌Id',
  `one_sort_id` CHAR(20) NOT NULL COMMENT '品牌Id',
  `picture` VARCHAR(255) DEFAULT NULL COMMENT '商品图片',
  `picture_name` VARCHAR(255) DEFAULT NULL COMMENT '商品图片名称',
  `price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '销售价格',
  `status` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '状态，0=下架，1=上架',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL  COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY (`brand_id`),
  KEY (`one_sort_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='商品信息表';



/* 分类属性分组表 */
CREATE TABLE `product_catelog_attrgroup` (
  `id` CHAR(20) NOT NULL COMMENT '属性分组ID',
  `title` CHAR(20) DEFAULT NULL COMMENT '属性名称',
  `one_sort_id` CHAR(20) NOT NULL COMMENT '属性所属分类ID',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY (`one_sort_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='分类属性分组表'


/* 属性描述 */
CREATE TABLE `product_catelog_attr` (
  `id` CHAR(20) NOT NULL COMMENT '属性描述ID',
  `value` CHAR(20) DEFAULT NULL COMMENT '属性描述内容',
  `attr_group_id` CHAR(20) NOT NULL COMMENT '属性分组id',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY (`attr_group_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='属性描述表'



/* 库存表 */
CREATE TABLE `product_stock` (
  `id` CHAR(20) NOT NULL COMMENT '主键ID',
  `product_id` CHAR(20) NOT NULL COMMENT '产品ID',
  `stock` bigint(10) NOT NULL DEFAULT 0 COMMENT '库存数量',
  `lock_stock` bigint(10) NOT NULL DEFAULT 0 COMMENT '锁定库存数量',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY (`product_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='库存表'

