package com.krest.admin.service;

import com.krest.admin.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-03
 */
public interface UserService extends IService<User> {

    User getUser(String name);
}
