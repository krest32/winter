package com.krest.admin.controller;


import com.krest.admin.entity.User;
import com.krest.admin.entity.Vo.LoginVo;
import com.krest.admin.service.RoleService;
import com.krest.admin.service.UserService;
import com.krest.utils.response.R;
import com.krest.utils.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 后台用户表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-03
 */
@CrossOrigin
@Api(value = "后台User相关接口",tags ="后台User相关接口")
@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * 列出所有的User信息
     */
    @ApiOperation(value = "列出所有的Author信息")
    @GetMapping("ListAllUser")
    public R ListAllUser(){
        List<User> list = userService.list(null);
        return R.ok().data("list",list);
    }



    /**
     * 后台登陆接口
     */
    @ApiOperation(value = "后台登陆接口")
    @PostMapping("login")
    public R login(@RequestBody LoginVo logvo){
        String name = logvo.getUsername();
        String password = logvo.getPassword();
        System.out.println(logvo);
        User user = userService.getUser(name);
        String password1 = user.getPassword();
        String id=user.getId();
        if (password1.equals(password)){
            String token = JwtUtils.getJwtToken(id,name);
            return R.ok().data("token",token);
        }else {
            return R.error().data("data","用户名密码错误");
        }
    }

    /**
     * 返回User信息
     */
    @ApiOperation(value = "返回User信息")
    @GetMapping("info")
    public R info(@RequestParam("token") String token){
        boolean flag = JwtUtils.checkToken(token);
        System.out.println(token);
        String s = JwtUtils.getuserInfoByJwtToken(token);
        User user = userService.getById(s);
        String roleId = user.getRoleId();
        String rolename = roleService.getById(roleId).getRolename();

        if (flag){
            return R.ok().data("roles",rolename).data("name",user.getUsername()).data("avatar",user.getAvatarAddress());
        }else{
            return R.error();
        }
    }

    /**
     * 登出，什么都不用做
     */
    @PostMapping("logout")
    @ApiOperation(value = "登出")
    public R logout(){
        return R.ok();
    }



}

