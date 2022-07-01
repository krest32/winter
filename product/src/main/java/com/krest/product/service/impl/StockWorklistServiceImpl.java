package com.krest.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krest.product.entity.Stock;
import com.krest.product.entity.StockWorklist;
import com.krest.product.entity.StockWorklistDetail;
import com.krest.product.mapper.StockWorklistMapper;
import com.krest.product.service.StockService;
import com.krest.product.service.StockWorklistDetailService;
import com.krest.product.service.StockWorklistService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 库存工作单 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-24
 */
@Service
public class StockWorklistServiceImpl extends ServiceImpl<StockWorklistMapper, StockWorklist> implements StockWorklistService {

    @Autowired
    private StockWorklistDetailService stockWorklistDetailService;

    @Autowired
    private StockService stockService;

    @Override
    public Integer feignCloseStockWorklist(String orderId) {
        QueryWrapper<StockWorklist> queryWrapper  = new QueryWrapper<>();
        queryWrapper.eq("order_id",orderId);
        List<StockWorklist> stockWorklists = baseMapper.selectList(queryWrapper);
        StockWorklist stockWorklist = stockWorklists.get(0);
        if (stockWorklist!=null){
            // 未支付设置库存工作单状态
            stockWorklist.setStatus(3);
            baseMapper.updateById(stockWorklist);
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public Integer feignChangeStockWorkStatus(String orderId) {
        QueryWrapper<StockWorklist> stockWorklistQueryWrapper = new QueryWrapper<>();
        stockWorklistQueryWrapper.eq("order_id",orderId);
        List<StockWorklist> stockWorklists = baseMapper.selectList(stockWorklistQueryWrapper);
        StockWorklist stockWorklist = stockWorklists.get(0);

        if (stockWorklist!=null){
            // 更改工作单状态
            stockWorklist.setStatus(1);
            baseMapper.updateById(stockWorklist);

            //更改详情单状态
            QueryWrapper<StockWorklistDetail> stockWorklistDetailQueryWrapper = new QueryWrapper<>();
            stockWorklistDetailQueryWrapper.eq("order_id",orderId);
            List<StockWorklistDetail> stockWorklistDetails = stockWorklistDetailService.list(stockWorklistDetailQueryWrapper);

            if (stockWorklistDetails.size()>0){
                for (StockWorklistDetail stockWorklistDetail : stockWorklistDetails) {
                    // 更新锁定库存信息
                    String productId = stockWorklistDetail.getProductId();
                    QueryWrapper<Stock> stockQueryWrapper = new QueryWrapper<>();
                    stockQueryWrapper.eq("product_id",productId);
                    List<Stock> list = stockService.list(stockQueryWrapper);
                    Stock stock = list.get(0);
                    stock.setLockStock(stock.getLockStock()-stockWorklistDetail.getLockNum());
                    stockService.updateById(stock);

                    //最后删除工作详情单
                    String id = stockWorklistDetail.getId();
                    stockWorklistDetailService.removeById(id);
                }
                return 1;
            }else {
                return 0;
            }
        }else{
            return 0;
        }

    }
}
