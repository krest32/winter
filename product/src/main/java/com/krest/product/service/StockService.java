package com.krest.product.service;

import com.krest.product.entity.Stock;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krest.product.entity.vo.OrderSubmitVo;
import com.krest.product.entity.vo.QueryStockVo;
import com.krest.product.entity.vo.ResultStockVo;
import com.krest.utils.entity.mqvo.StockLockedVo;

import java.util.List;

/**
 * <p>
 * 库存表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-14
 */
public interface StockService extends IService<Stock> {

    ResultStockVo queryPageStock(Long page, Long limit, QueryStockVo queryStockVo);

    void flushStock();

    Stock getStockById(String id);

    void updateStock(Stock stock);

    List<Stock> getStockByProductId(String id);

    void feignLockStockById(String id, Long num);

    Integer feignLockStockByLists(OrderSubmitVo orderSubmitVo);

    void releaseLockStock(StockLockedVo stockLockedVo);
}
