package com.krest.product.controller;


import com.krest.product.entity.Stock;
import com.krest.product.entity.vo.OrderSubmitVo;
import com.krest.product.entity.vo.QueryStockVo;
import com.krest.product.entity.vo.ResultStockVo;
import com.krest.product.service.StockService;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 库存表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-14
 */
@Api(value = "库存管理",tags ="库存管理")
@CrossOrigin
@RestController
@RequestMapping("/product/stock")
public class StockController {


    @Autowired
    private StockService stockService;

    @Autowired
    RabbitTemplate rabbitTemplate;


    @ApiOperation("测试Rabbitmq")
    @GetMapping("testMQ")
    public R testMQ(){
        Stock stock = new Stock();
        stock.setId("123");
        rabbitTemplate.convertAndSend("stock-event-exchange","stock.locked",stock);
        return R.ok().data("productOrder",stock);
    }


    @ApiOperation(value = "根据产品Id集合，批量锁定库存")
    @PostMapping("feignLockStockByLists")
    public Integer feignLockStockByLists(@RequestBody OrderSubmitVo orderSubmitVo){
        Integer integer = stockService.feignLockStockByLists(orderSubmitVo);
        return integer;
    }



    @ApiOperation(value = "根据产品Id锁定库存")
    @GetMapping("feignLockStockById/{id}/{num}")
    public R feignLockStockById(@PathVariable String id,
                                    @PathVariable Long num){
        stockService.feignLockStockById(id,num);
        return R.ok();
    }

    @ApiOperation(value = "根据Id得到库存信息")
    @GetMapping("feignGetStockById/{id}")
    public Stock feignGetStockById(@PathVariable String id){
        List<Stock> stocks=stockService.getStockByProductId(id);
        Stock stock=stocks.get(0);
        return stock;
    }

    @ApiOperation(value = "库存列表")
    @PostMapping("queryPageStock/{page}/{limit}")
    public R queryPageStock(@PathVariable Long page,
                       @PathVariable Long limit,
                       @RequestBody(required = false) QueryStockVo queryStockVo){
        ResultStockVo resultStockVo =  stockService.queryPageStock(page,limit,queryStockVo);
        return R.ok().data("result",resultStockVo);
    }


    @ApiOperation(value = "刷新库存")
    @GetMapping("flushStock")
    public R flushStock(){
        stockService.flushStock();
        return R.ok();
    }

    @ApiOperation(value = "根据Id得到库存信息")
    @GetMapping("getStockById/{id}")
    public R getStockById(@PathVariable String id){
        Stock stock=stockService.getStockById(id);
        return R.ok().data("stock",stock);
    }


    @ApiOperation(value = "更新库存")
    @PostMapping("updateStock")
    public R updateStock(@RequestBody Stock stock){
        stockService.updateStock(stock);
        return R.ok();
    }

    @ApiOperation(value = "根据产品Id得到库存信息")
    @GetMapping("getStockByProductId/{id}")
    public R getStockByProductId(@PathVariable String id){
        List<Stock> stocks=stockService.getStockByProductId(id);
        return R.ok().data("stocks",stocks);
    }
}

