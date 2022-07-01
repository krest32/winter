CREATE TABLE `blog` (
  `id` CHAR(20) NOT NULL COMMENT 'blog主键ID',
  `title` CHAR(200) DEFAULT NULL COMMENT '博客标题',
  `summary` CHAR(255) DEFAULT NULL COMMENT '博客简介',
  `content` LONGTEXT COMMENT '博客内容',
  `picture_uid` VARCHAR(255) DEFAULT NULL COMMENT '标题图片id',
  `status` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '状态，0=草稿，1=发布',
  `author_id` VARCHAR(255) DEFAULT NULL COMMENT '作者id',
  `blog_sort_id` VARCHAR(32) DEFAULT NULL COMMENT '博客分类ID',
  `price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '销售价格，设置为0则可免费观看',
  `open_comment` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '是否开启评论(0:否 1:是)',
  `puy_count` bigint(10) DEFAULT '0' COMMENT '购买人数',
  `collect_count` bigint(10) DEFAULT '0' COMMENT '收藏人数',
  `click_count` bigint(10) DEFAULT '0' COMMENT '点击人数',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='博客表';
/* 为Id列添加索引，避免通过数据库遍历查找信息*/
ALTER TABLE blog ADD INDEX (picture_uid);
ALTER TABLE blog ADD INDEX (author_id);
ALTER TABLE blog ADD INDEX (blog_sort_id);


/* blog作者表 */
CREATE TABLE `blog_author` (
  `id` CHAR(20) NOT NULL COMMENT 'author主键ID',
  `name` CHAR(200) DEFAULT NULL COMMENT '作者名字',
  `sex` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '性别，0=男，1=女',
  `intro` CHAR(255) DEFAULT NULL COMMENT '一句话简介',
  `summary` CHAR(255) DEFAULT NULL COMMENT '作者详细介绍',
  `picture_uid` VARCHAR(255) DEFAULT NULL COMMENT '作者头像图片Id',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL  COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='作者信息表';



/* blog标签表 */
CREATE TABLE `blog_tag` (
  `id` CHAR(20) NOT NULL COMMENT 'tag主键ID',
  `title` CHAR(200) DEFAULT NULL COMMENT '标签名称',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='标签表';

/* blog标签表 */
CREATE TABLE `blog_tag_relation` (
  `id` CHAR(20) NOT NULL COMMENT '主键ID',
  `blog_id` CHAR(200) DEFAULT NULL COMMENT '博客id',
  `tag_id` CHAR(200) DEFAULT NULL COMMENT '标签id',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY (`blog_id`),
  KEY (`tag_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='博客标签关系表';


/* blog图片表 */
CREATE TABLE `blog_picture` (
  `id` CHAR(20) NOT NULL COMMENT 'picture主键ID',
  `filename` CHAR(255) DEFAULT NULL COMMENT '图片名称',
  `avatar` CHAR(255) DEFAULT NULL COMMENT 'picture地址',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='图片信息表';



/* blog分类表 */
CREATE TABLE `blog_sort` (
  `id` CHAR(20) NOT NULL COMMENT '主键ID',
  `title` CHAR(255) DEFAULT NULL COMMENT 'blog分类名称',
  `content` char(255) COMMENT '分类简介',
  `picture_uid` VARCHAR(255) DEFAULT NULL COMMENT '分类图片Id',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY (`picture_uid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='blog分类表';
