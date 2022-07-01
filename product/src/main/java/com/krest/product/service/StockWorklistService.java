package com.krest.product.service;

import com.krest.product.entity.StockWorklist;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 库存工作单 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-24
 */
public interface StockWorklistService extends IService<StockWorklist> {

    Integer feignCloseStockWorklist(String orderId);

    Integer feignChangeStockWorkStatus(String orderId);
}
