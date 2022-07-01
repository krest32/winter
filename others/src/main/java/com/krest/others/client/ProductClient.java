package com.krest.others.client;

import com.krest.others.entity.OrderSubmitVo;
import com.krest.others.entity.Stock;
import com.krest.utils.entity.Product;
import com.krest.utils.response.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/11/24 14:49
 * @Description:
 */
@Component
@FeignClient("product")
public interface ProductClient {

    @ApiOperation(value = "根据Id的商品的详细信息")
    @GetMapping("/product/product/getOrderProductById/{id}")
    public Product getOrderProductById(@PathVariable("id") String id);

    @ApiOperation(value="获取商品的价格")
    @PostMapping("/product/product/getPriceById/{id}")
    public BigDecimal getPriceById(@PathVariable("id") String id);


    @ApiOperation(value = "根据Id得到库存信息")
    @GetMapping("/product/stock/feignGetStockById/{id}")
    public Stock feignGetStockById(@PathVariable("id") String id);

    @ApiOperation(value = "根据产品Id锁定库存")
    @GetMapping("/product/stock/feignLockStockById/{id}/{num}")
    public R feignLockStockById(@PathVariable("id") String id,
                                @PathVariable("num") Long num);

    @ApiOperation(value = "根据产品Id集合，批量锁定库存")
    @PostMapping("/product/stock/feignLockStockByLists")
    public Integer feignLockStockByLists(@RequestBody OrderSubmitVo orderSubmitVo);


    @ApiOperation(value = "超时关闭库存工作单")
    @GetMapping("/product/stock-worklist/feignCloseStockWorklist/{orderId}")
    public Integer feignCloseStockWorklist(@PathVariable("orderId") String orderId);

    @ApiOperation(value = "修改库存工作单和详情单的状态")
    @GetMapping("/product/stock-worklist/feignChangeStockWorkStatus/{orderId}")
    public Integer feignChangeStockWorkStatus(@PathVariable("orderId") String orderId);
}
