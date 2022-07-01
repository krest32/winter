
/* winter后台用户管理表 */
CREATE TABLE `admin_user` (
  `id` CHAR(20) NOT NULL COMMENT '用户主键ID',
  `username` CHAR(20) DEFAULT NULL COMMENT '用户名称',
  `password` CHAR(100) DEFAULT NULL COMMENT '用户密码',
  `phone` CHAR(20) DEFAULT NULL COMMENT '用户手机号码',
  `role_id` CHAR(20) DEFAULT NULL COMMENT '角色id',
  `avatar_address` VARCHAR(255) DEFAULT NULL COMMENT '头像图片地址',
  `avatar_name` VARCHAR(255) DEFAULT NULL COMMENT '头像图片名称',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY (`role_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='后台用户表';


/* winter后台用户管理表 */
CREATE TABLE `admin_role` (
  `id` CHAR(20) NOT NULL COMMENT '角色主键ID',
  `rolename` CHAR(20) DEFAULT NULL COMMENT '角色名称',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0=未删除，1=删除',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='用户角色表';