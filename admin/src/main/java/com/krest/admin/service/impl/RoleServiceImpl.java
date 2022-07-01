package com.krest.admin.service.impl;

import com.krest.admin.entity.Role;
import com.krest.admin.mapper.RoleMapper;
import com.krest.admin.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-03
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
