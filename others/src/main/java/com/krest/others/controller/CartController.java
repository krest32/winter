package com.krest.others.controller;


import com.krest.others.service.CartService;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: krest
 * @Date: 2020/11/20 12:18
 * @Description:
 */

@Api(value = "购物车功能",tags ="购物车功能")
@CrossOrigin
@RestController
@RequestMapping("/others/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    //跳转到购物车列表页：得到所有的购物车信息
    @GetMapping("getCartList")
    public R cartListPage(HttpServletRequest request){
        return cartService.cartListPage(request);
    }

    @ApiOperation(value = "添加商品到购物车")
    @GetMapping("addCartItem/{productId}/{num}")
    public R addCartItem(@PathVariable String  productId,
                         @PathVariable Integer num,
                         HttpServletRequest request){
        return cartService.addCartItem(productId,num,request);
    }

    @ApiOperation(value = "修改购物车中商品的数量")
    @GetMapping("changeCartItemNum/{productId}/{num}")
    public R changeCartItemNum(@PathVariable String  productId,
                         @PathVariable Integer num,
                         HttpServletRequest request){
        return cartService.changeCartItemNum(productId,num,request);
    }

    @ApiOperation(value = "根据商品Id删除商品")
    @DeleteMapping("deleteCartItem/{productId}")
    public R deleteCartItem(@PathVariable String  productId,
                            HttpServletRequest request){
        return cartService.deleteCartItem(productId,request);
    }
}
