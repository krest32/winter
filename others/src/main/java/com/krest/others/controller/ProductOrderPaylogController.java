package com.krest.others.controller;


import com.krest.others.client.ProductClient;
import com.krest.others.entity.ProductOrder;
import com.krest.others.entity.ProductOrderPaylog;
import com.krest.others.service.ProductOrderPaylogService;
import com.krest.others.service.ProductOrderService;
import com.krest.utils.myexception.myException;
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
 * @since 2020-12-24
 */

@CrossOrigin
@Api(value = "支付日志",tags ="支付日志")
@RestController
@RequestMapping("/others/product-order-paylog")
public class ProductOrderPaylogController {

    @Autowired
    private ProductOrderPaylogService paylogService;

    @Autowired
    private ProductOrderService orderService;

    @Autowired
    private ProductClient productClient;



    @ApiOperation(value = "生成微信支付二维码" )
    @GetMapping("/createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo) {
        Map map = paylogService.createNative(orderNo);
        return R.ok().data(map);
    }

    @ApiOperation(value = "查询支付状态" )
    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {
        //调用查询接口
        Map<String, String> map = paylogService.queryPayStatus(orderNo);
        System.out.println(map);

        //出错
        if (map == null) {
            return R.error().message("支付出错");
        }
        //如果成功
        ProductOrder byId = orderService.getById(orderNo);

        if (byId.getStatus()==1){
            return R.ok().data("order",byId);
        }

        if (map.get("trade_state").equals("SUCCESS") && byId.getStatus()!=1) {
            //更改订单支付状态
            paylogService.updateOrderStatus(map);

            // 更改订单状态

            byId.setStatus(1);
            orderService.updateById(byId);

            // 更改订单工作单的状态
            Integer integer = productClient.feignChangeStockWorkStatus(orderNo);

            return R.ok().data("order",byId);

        }
        return R.ok().data("order",byId);
    }

}

