package com.krest.others.controller;


import com.krest.others.entity.CartVo;
import com.krest.others.entity.OrderSubmitVo;
import com.krest.others.entity.ProductOrder;
import com.krest.others.entity.SubmitOrderVo;
import com.krest.others.entity.vo.QueryOrder;
import com.krest.others.service.ProductOrderService;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-16
 */

@CrossOrigin
@Api(value = "商品订单",tags ="商品订单")
@RestController
@RequestMapping("/others/product-order")
public class ProductOrderController {

    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    RabbitTemplate rabbitTemplate;


    @ApiOperation("根据id查询订单")
    @GetMapping("feignQueryOrderById/{id}")
    public R QueryOrderById(@PathVariable String id){
        ProductOrder order = productOrderService.getById(id);
        return R.ok().data("order",order);
    }

    @ApiOperation("测试Rabbitmq")
    @GetMapping("testMQ")
    public R testMQ(){
        ProductOrder order = new ProductOrder();
        order.setId("123");
        rabbitTemplate.convertAndSend("order-event-exchange","order.create.order",order);
        return R.ok().data("productOrder",order);
    }

    @ApiOperation("生成新的订单")
    @GetMapping("createOrder/{memberId}")
    public R createOrder(@PathVariable String memberId){
        ProductOrder order = productOrderService.createOrder(memberId);
        return R.ok().data("productOrder",order);
    }


    @ApiOperation("提交订单")
    @PostMapping("submitOrder")
    public R submitOrder(@RequestBody OrderSubmitVo orderSubmitVo){
        SubmitOrderVo submitOrderVo = productOrderService.submitOrder(orderSubmitVo);
        if (submitOrderVo.getCode()==1){
            return R.ok().data("submitOrderVo",submitOrderVo);
        }else{
            return R.error().data("submitOrderVo",submitOrderVo);
        }
    }

    @ApiOperation("根据会员id查询订单")
    @GetMapping("getOrderList/{memberId}")
    public R getOrderList(@PathVariable String  memberId){
        List<ProductOrder> list= productOrderService.getOrderList(memberId);
        return R.ok().data("list",list);
    }

    @ApiOperation("查询所有订单")
    @PostMapping("listOrderList/{page}/{limit}")
    public R listOrderList(@PathVariable Long page,
                          @PathVariable Long limit,
                          @RequestBody(required = false) QueryOrder queryOrder){
        return productOrderService.listOrderList(page,limit,queryOrder);
    }

    @ApiOperation("根据id删除订单")
    @GetMapping("deleteOrderById/{id}")
    public R deleteOrderById(@PathVariable String id){
        return productOrderService.deleteOrderById(id);
    }



}

