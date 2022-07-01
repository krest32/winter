package com.krest.member.controller;


import com.krest.member.service.PayLogService;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Api(value = "支付日志",tags ="支付日志")
@CrossOrigin
@RestController
@RequestMapping("/member/pay-log")
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    @ApiOperation(value = "生成微信支付二维码" )
    @GetMapping("/createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo) {
        Map map = payLogService.createNative(orderNo);
        return R.ok().data(map);
    }

    @ApiOperation(value = "查询支付状态" )
    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {
        //调用查询接口
        Map<String, String> map = payLogService.queryPayStatus(orderNo);
        System.out.println(map);
        //出错
        if (map == null) {
            return R.error().message("支付出错");
        }
        //如果成功
        if (map.get("trade_state").equals("SUCCESS")) {
            //更改订单状态
            payLogService.updateOrderStatus(map);
            return R.ok().code(20000).message("支付成功");
        }

        return R.ok().code(20001).message("支付中");
    }

}

