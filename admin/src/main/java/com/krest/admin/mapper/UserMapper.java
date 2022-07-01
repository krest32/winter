package com.krest.admin.mapper;

import com.krest.admin.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author krest
 * @since 2020-12-03
 */
public interface UserMapper extends BaseMapper<User> {

     User getUser(String username);
}
