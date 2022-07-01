package com.krest.others.service;

import com.krest.others.entity.ProductOrderPaylog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-24
 */
public interface ProductOrderPaylogService extends IService<ProductOrderPaylog> {

    Map createNative(String orderNo);

    Map<String, String> queryPayStatus(String orderNo);

    void updateOrderStatus(Map<String, String> map);
}
