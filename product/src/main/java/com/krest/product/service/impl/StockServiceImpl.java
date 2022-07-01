package com.krest.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krest.product.client.AliyunClient;
import com.krest.product.entity.Product;
import com.krest.product.entity.Stock;
import com.krest.product.entity.StockWorklist;
import com.krest.product.entity.StockWorklistDetail;
import com.krest.product.entity.vo.*;
import com.krest.product.mapper.StockMapper;
import com.krest.product.service.ProductService;
import com.krest.product.service.StockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.product.service.StockWorklistDetailService;
import com.krest.product.service.StockWorklistService;
import com.krest.utils.entity.MQOrder;
import com.krest.utils.entity.ProductOrder;
import com.krest.utils.entity.mqvo.StockLockDetail;
import com.krest.utils.entity.mqvo.StockLockedVo;
import com.krest.utils.myexception.myException;
import com.krest.utils.utils.IdWorker;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 库存表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-14
 */
@RabbitListener(queues = "stock.release.queue")
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {

    @Autowired
    private StockWorklistService stockWorklistService;

    @Autowired
    private StockWorklistDetailService stockWorklistDetailService;

    @Autowired
    private AliyunClient aliyunClient;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private ProductService productService;

    public static IdWorker idWorker = new IdWorker();

    /**
     * 监听超时订单，并自动取消
     */
    @RabbitHandler
    public void handleStockLockRelease(StockLockedVo stockLockedVo, Message message, Channel channel) throws IOException {

        try{
            System.out.println("收到库存解锁的消息");
            String id = stockLockedVo.getId();
            StockWorklist stockWorklist = stockWorklistService.getById(id);
            StockLockDetail stockLockDetail = stockLockedVo.getStockLockDetail();
            String stockLockDetailId = stockLockDetail.getId();
            Object object = aliyunClient.QueryOrderById(stockLockedVo.getStockLockDetail().getOrderId()).getData().get("order");
            ProductOrder productOrder = JSON.parseObject(JSON.toJSONString(object), new TypeReference<ProductOrder>() { });
            //查询是否已经有了相应的订单
            // 如果所有的信息不为空，且订单没有被支付，可以执行库存的回滚操作
            // 情况分类：1.订单超时，自动取消、  2. 订单被用户自己取消  3. 库存锁定异常自动解锁库存
            if (productOrder.getStatus() == 1 && stockWorklist.getStatus()==1 ){
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }else{
                // 如果没有stockWorklist，说明说库存失败
                // 如果没有productOrder ，说明订单创建失败
                // productOrder.getStatus() == 3 && stockWorklist.getStatus()==3 说明订单失效，需要解锁库存
                if( stockWorklist == null || productOrder ==null || (productOrder.getStatus() == 3 && stockWorklist.getStatus()==3 )){
                    // 检查库存工作详情单是否还存在
                    StockWorklistDetail stockWorklistDetail = stockWorklistDetailService.getById(stockLockDetailId);
                    if (stockWorklistDetail!=null){
                        System.out.println("执行库存自动解锁");
                        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("product_id",stockLockDetail.getProductId());
                        List<Stock> stocks = baseMapper.selectList(queryWrapper);
                        Stock stock = stocks.get(0);
                        // 减去锁定库存
                        stock.setLockStock(stock.getLockStock()-stockLockDetail.getLockNum());
                        // 添加原来库存
                        stock.setStock(stock.getStock()+stockLockDetail.getLockNum());
                        baseMapper.updateById(stock);
                        // 然后删除之前的锁定库存单
                        stockWorklistDetailService.removeById(stockLockDetailId);
                    }
                    // 最后删除 锁定库存工作单
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                }else {
                    System.out.println("自动关单失败");
                    throw new myException(20001,"自动关单失败");
                }
            }
        }catch (Exception e){
            System.out.println("关闭库存失败，消息重新回到队列中");
            // 将消息重新发到队列当中
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }

    /**
     * 防止订单卡顿，库存一直无法释放,相当于二次确认
     */
    @RabbitHandler
    public void handleOrderRelease(MQOrder order, Message message, Channel channel) throws IOException {
        try{
            System.out.println("收到订单释放的消息");
            //先查询订单状态
            String orderId = order.getId();
            // 查询最新订单信息
            Object object =  aliyunClient.QueryOrderById(orderId).getData().get("order");
            ProductOrder productOrder = JSON.parseObject(JSON.toJSONString(object), new TypeReference<ProductOrder>() { });
            // 查询最新订单工作单信息
            QueryWrapper<StockWorklist> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id",orderId);
            List<StockWorklist> stockWorklists = stockWorklistService.list(queryWrapper);
            StockWorklist stockWorklist = stockWorklists.get(0);
            // 查询最新订单工作单详情信息
            QueryWrapper<StockWorklistDetail> stockWorklistDetailQueryWrapper = new QueryWrapper<>();
            stockWorklistDetailQueryWrapper.eq("order_id", orderId);
            int count = stockWorklistDetailService.count(stockWorklistDetailQueryWrapper);

            if(productOrder.getStatus()==1 && stockWorklist.getStatus()==1){
                System.out.println("订单已经支付，无需自动关单");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }

            if ( productOrder.getStatus()==3 && stockWorklist.getStatus()==3  && count >0 ){
                System.out.println("开始通过订单关闭信息解锁库存");
                List<StockWorklistDetail> list = stockWorklistDetailService.list(stockWorklistDetailQueryWrapper);
                for (StockWorklistDetail stockWorklistDetail : list) {
                    QueryWrapper<Stock> queryWrapperStock = new QueryWrapper<>();
                    queryWrapperStock.eq("product_id",stockWorklistDetail.getProductId());
                    List<Stock> stocks = baseMapper.selectList(queryWrapperStock);
                    Stock stock = stocks.get(0);
                    // 减去锁定库存
                    stock.setLockStock(stock.getLockStock()-stockWorklistDetail.getLockNum());
                    // 添加原来库存
                    stock.setStock(stock.getStock()+stockWorklistDetail.getLockNum());
                    baseMapper.updateById(stock);
                    // 然后删除之前的锁定库存单
                    stockWorklistDetailService.removeById(stockWorklistDetail.getId());
                }
                System.out.println("通过订单关闭信息解锁库存====完成");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }else{
                System.out.println("订单系统已经自动完成");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }
            // 最后删除 锁定库存工作单
        }catch (Exception e){
            System.out.println("订单关闭库存失败，消息重新回到队列中");
            // 将消息重新发到队列当中
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }

    @Override
    public ResultStockVo queryPageStock(Long page, Long limit, QueryStockVo queryStockVo) {

        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        Page<Stock> stockPage = new Page<>(page,limit);
        ResultStockVo resultStockVo =new ResultStockVo();


        String productTitle = queryStockVo.getProductTitle();
        List<String> pIds = new ArrayList<>();

        if (!StringUtils.isEmpty(productTitle)){
            QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
            productQueryWrapper.like("title",productTitle);
            List<Product> products = productService.list(productQueryWrapper);
            for (Product product : products) {
                pIds.add(product.getId());
            }

            List<Stock> stocks = baseMapper.selectBatchIds(pIds);
            for (String pId : pIds) {
                QueryWrapper<Stock> stockQueryWrapper = new QueryWrapper<>();
                stockQueryWrapper.eq("product_id",pId);
                List<Stock> stocks1 = baseMapper.selectList(stockQueryWrapper);
                stocks.addAll(stocks1);
            }

            List<Stock> collect = stocks.stream().map(stock -> {
                return stock.setStockTotal(stock.getLockStock()+stock.getStock());
            }).collect(Collectors.toList());

            resultStockVo.setStockList(collect);

        }else{
            IPage<Stock> stockIPage = baseMapper.selectPage(stockPage, queryWrapper);
            Long total = stockIPage.getTotal();
            List<Stock> records = stockIPage.getRecords();

            List<Stock> collect = records.stream().map(stock -> {
                return stock.setStockTotal(stock.getLockStock()+stock.getStock());
            }).collect(Collectors.toList());

            resultStockVo.setTotal(total);
            resultStockVo.setStockList(collect);

        }

        return resultStockVo;

    }

    @Override
    public void flushStock() {
        List<Product> products = productService.list(null);
        List<Stock> stocks = baseMapper.selectList(null);

        List<String> pIds = new ArrayList<>();
        List<String> productIds = new ArrayList<>();

        // 添加商品信息到库存表
        for (Stock stock : stocks) {
            String pId = stock.getProductId();
            pIds.add(pId);
        }

        for (Product product : products) {
            String productId = product.getId();
            productIds.add(productId);
            if (!pIds.contains(productId)){
                Stock stock = new Stock();
                stock.setId(idWorker.nextId());
                stock.setProductId(productId);
                stock.setProductTitle(product.getTitle());
                stock.setStock(0L);
                stock.setLockStock(0L);
                baseMapper.insert(stock);
            }
        }

        // 从库存表中删除多余的商品信息
        for (Stock stock : stocks) {
            String pId = stock.getProductId();
            if (!productIds.contains(pId)){
                baseMapper.deleteById(pId);
            }
        }
    }

    @Override
    public Stock getStockById(String id) {
        Stock stock = baseMapper.selectById(id);
        stock.setStockTotal(stock.getLockStock()+stock.getStock());
        return stock;
    }

    @Override
    public void updateStock(Stock stock) {
        baseMapper.updateById(stock);
    }


    @Override
    public List<Stock> getStockByProductId(String id) {
        QueryWrapper<Stock> stockQueryWrapper = new QueryWrapper<>();
        stockQueryWrapper.eq("product_id",id);
        List<Stock> stocks1 = baseMapper.selectList(stockQueryWrapper);
        if (stocks1.size()==1){
            stocks1.get(0).setStockTotal(stocks1.get(0).getLockStock()+stocks1.get(0).getStock());
            return stocks1;
        }else {
            throw new myException(20001,"获取产品库存错误");
        }
    }

    @Override
    public void feignLockStockById(String id, Long num) {
        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id",id);
        List<Stock> stocks = baseMapper.selectList(queryWrapper);
        Stock stock =stocks.get(0);
        stock.setStock(stock.getStock()-num);
        stock.setLockStock(stock.getLockStock()+num);
        baseMapper.updateById(stock);
    }

    @Override
    public Integer feignLockStockByLists(OrderSubmitVo orderSubmitVo) {
        List<OrderItemVo> orderItemVos = orderSubmitVo.getOrderVo().getorderItemVoList();
        Integer result = 1;

        // 创建订单消息工作单
        StockWorklist stockWorklist = new StockWorklist();
        String stockWorklistId = idWorker.nextId();
        stockWorklist.setId(stockWorklistId);
        stockWorklist.setOrderId(orderSubmitVo.getId());

        for (OrderItemVo orderItemVo : orderItemVos) {
            String productId = orderItemVo.getProductId();
            Integer countNum = orderItemVo.getCountNum();
            QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("product_id",productId);
            List<Stock> stocks = baseMapper.selectList(queryWrapper);
            Stock stock =stocks.get(0);
            Long num = stock.getStock()-countNum;

            // 锁定成功
            if (num>=0){
                stock.setStock(num);
                stock.setLockStock(stock.getLockStock()+countNum);
                baseMapper.updateById(stock);

                // 保存库存锁定的详情单
                StockWorklistDetail stockWorklistDetail = new StockWorklistDetail();
                stockWorklistDetail.setId(idWorker.nextId());
                stockWorklistDetail.setOrderId(orderSubmitVo.getId());
                stockWorklistDetail.setProductId(orderItemVo.getProductId());
                stockWorklistDetail.setProductTitle(orderItemVo.getProductTitle());
                long value = countNum.longValue();
                stockWorklistDetail.setLockNum(value);
                stockWorklistDetailService.save(stockWorklistDetail);

                // 向 RabbitMq中发送消息，锁定库存的消息
                StockLockedVo stockLockedVo = new StockLockedVo();
                stockLockedVo.setId(stockWorklistId);
                StockLockDetail stockLockDetail = new StockLockDetail();
                BeanUtils.copyProperties(stockWorklistDetail,stockLockDetail);
                stockLockedVo.setStockLockDetail(stockLockDetail);
                rabbitTemplate.convertAndSend("stock-event-exchange","stock.locked",stockLockedVo);
                // 库存锁定成功返回 1
                }else{
                // 库存锁定失败返回0
                return 0;
            }

        }
        stockWorklistService.save(stockWorklist);
        return result;
    }

    @Override
    public void releaseLockStock(StockLockedVo stockLockedVo) {

    }
}
