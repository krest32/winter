package com.krest.others.service;

import com.krest.others.entity.CartVo;
import com.krest.others.entity.OrderSubmitVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krest.others.entity.ProductOrder;
import com.krest.others.entity.SubmitOrderVo;
import com.krest.others.entity.vo.QueryOrder;
import com.krest.utils.response.R;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-16
 */
public interface ProductOrderService extends IService<ProductOrder> {

    ProductOrder createOrder(String memberId);

    SubmitOrderVo submitOrder(OrderSubmitVo orderSubmitVo);

    List<ProductOrder> getOrderList(String memberId);

    R listOrderList(Long page, Long limit, QueryOrder queryOrder);

    R deleteOrderById(String id);
}
