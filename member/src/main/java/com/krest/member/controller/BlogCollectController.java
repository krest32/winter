package com.krest.member.controller;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员博客收藏表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Api(value = "会员中心",tags ="会员中心")
@CrossOrigin
@RestController
@RequestMapping("/member/blog-collect")
public class BlogCollectController {

}

