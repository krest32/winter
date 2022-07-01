package com.krest.others.service;

import com.krest.others.entity.CartVo;
import com.krest.utils.response.R;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: krest
 * @Date: 2020/11/20 12:19
 * @Description:
 */
public interface CartService {
    R cartListPage(HttpServletRequest request);

    R addCartItem(String productId, Integer num, HttpServletRequest request);

    R changeCartItemNum(String productId, Integer num, HttpServletRequest request);

    R deleteCartItem(String productId,HttpServletRequest request);

    CartVo getCartVo(String memberId);
}
