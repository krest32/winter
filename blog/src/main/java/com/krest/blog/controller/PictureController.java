package com.krest.blog.controller;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 图片信息表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
@CrossOrigin
@Api(value = "图片管理",tags ="图片管理")
@RestController
@RequestMapping("/blog/picture")
public class PictureController {

}

