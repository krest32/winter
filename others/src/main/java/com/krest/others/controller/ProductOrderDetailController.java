package com.krest.others.controller;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单详情表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-16
 */

@CrossOrigin
@Api(value = "订单详情",tags ="订单详情")
@RestController
@RequestMapping("/others/product-order-detail")
public class ProductOrderDetailController {

}

