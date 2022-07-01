
CREATE TABLE `vedio_catelog` (
  `id` CHAR(20) NOT NULL COMMENT '主键ID',
  `title` CHAR(255) DEFAULT NULL COMMENT '分类名称',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='视频分类表';


CREATE TABLE `vedio_album` (
  `id` CHAR(20) NOT NULL COMMENT '专辑ID',
  `title` CHAR(255) DEFAULT NULL COMMENT '专辑名称',
  `intro` char(255) COMMENT '专辑简介',
  `picture_id` VARCHAR(255) DEFAULT NULL COMMENT '图片Id',
  `puy_count` bigint(10) DEFAULT '0' COMMENT '购买人数',
  `collect_count` bigint(10) DEFAULT '0' COMMENT '收藏人数',
  `click_count` bigint(10) DEFAULT '0' COMMENT '点击人数',
  `play_count` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '播放次数',
  `is_free` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否可以试听：0收费 1免费',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY (`picture_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='专辑表';

CREATE TABLE `vedio_picture` (
  `id` CHAR(20) NOT NULL COMMENT 'picture主键ID',
  `filename` CHAR(255) DEFAULT NULL COMMENT '图片名称',
  `address` CHAR(255) DEFAULT NULL COMMENT 'picture链接',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='视频图片信息表';

CREATE TABLE `vedio` (
  `id` char(19) NOT NULL COMMENT '视频ID',
  `album_id` char(19) NOT NULL COMMENT '专辑Id',
  `catelog_id` char(19) NOT NULL COMMENT '分类Id',
  `title` varchar(50) NOT NULL COMMENT '视频名称',
  `video_source_id` varchar(100) DEFAULT NULL COMMENT '云端视频资源',
  `video_original_name` varchar(100) DEFAULT NULL COMMENT '原始文件名称',
  `play_count` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '播放次数',
  `is_free` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否可以试听：0收费 1免费',
  `duration` float NOT NULL DEFAULT '0' COMMENT '视频时长（秒）',
  `size` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '视频源文件大小（字节）',
  `price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '销售价格，设置为0则可免费观看',
  `puy_count` bigint(10) DEFAULT '0' COMMENT '购买人数',
  `collect_count` bigint(10) DEFAULT '0' COMMENT '收藏人数',
  `click_count` bigint(10) DEFAULT '0' COMMENT '点击人数',
  `version` bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY(`album_id`),
  KEY(`catelog_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='视频信息表';

