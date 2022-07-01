package com.krest.member.service;

import com.krest.member.entity.OrderVedio;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krest.member.entity.Vo.QueryVideoOrderVo;
import com.krest.utils.response.R;

/**
 * <p>
 * 视频购买订单 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
public interface OrderVedioService extends IService<OrderVedio> {

    String createOrderVedio(OrderVedio orderVedio);

    OrderVedio getOrderVedioInfo(String id);

    R listVideoOrder(Long page, Long limit, QueryVideoOrderVo queryVideoOrderVo);
}
