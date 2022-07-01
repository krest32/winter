package com.krest.admin.service.impl;

import com.krest.admin.entity.User;
import com.krest.admin.mapper.UserMapper;
import com.krest.admin.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getUser(String name) {
        User user = baseMapper.getUser(name);
        if (user==null) {
            throw new RuntimeException("没有该用户");
        }else{
            return user;
        }

    }
}
