package com.krest.member.controller;


import com.krest.member.entity.Member;
import com.krest.member.entity.OrderVedio;
import com.krest.member.entity.Vo.QueryVideoOrderVo;
import com.krest.member.service.OrderVedioService;
import com.krest.utils.entity.Album;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 视频购买订单 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Api(value = "订单管理",tags ="订单管理")
@CrossOrigin
@RestController
@RequestMapping("/member/order-vedio")
public class OrderVedioController {

    @Autowired
    private OrderVedioService orderVedioService;

    @ApiOperation(value = "创建新订单")
    @PostMapping("createOrderVedio")
    public R createOrderVedio(@RequestBody OrderVedio orderVedio){
        String orderId = orderVedioService.createOrderVedio(orderVedio);
        return R.ok().data("orderId",orderId);
    }

    @ApiOperation(value = "根据订单Id查询信息")
    @GetMapping("getOrderVedioInfo/{id}")
    public R getOrderVedioInfo(@PathVariable String id){
        OrderVedio order = orderVedioService.getOrderVedioInfo(id);
        return R.ok().data("order",order);
    }

    @ApiOperation(value = "查询所有订单")
    @PostMapping("listVideoOrder/{page}/{limit}")
    public R listVideoOrder(@PathVariable Long page,
                            @PathVariable Long limit,
                            @RequestBody(required = false) QueryVideoOrderVo queryVideoOrderVo){
        return orderVedioService.listVideoOrder(page,limit,queryVideoOrderVo);
    }





}

