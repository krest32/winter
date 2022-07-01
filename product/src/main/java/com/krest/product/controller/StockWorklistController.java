package com.krest.product.controller;


import com.krest.product.entity.vo.OrderSubmitVo;
import com.krest.product.service.StockWorklistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 库存工作单 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-24
 */
@Api(value = "库存工作单",tags ="库存工作单")
@CrossOrigin
@RestController
@RequestMapping("/product/stock-worklist")
public class StockWorklistController {

    @Autowired
    private StockWorklistService stockWorklistService;

    @ApiOperation(value = "超时关闭库存工作单")
    @GetMapping("feignCloseStockWorklist/{orderId}")
    public Integer feignCloseStockWorklist(@PathVariable String orderId){
        Integer integer = stockWorklistService.feignCloseStockWorklist(orderId);
        return integer;
    }


    @ApiOperation(value = "修改库存工作单和详情单的状态")
    @GetMapping("feignChangeStockWorkStatus/{orderId}")
    public Integer feignChangeStockWorkStatus(@PathVariable String orderId){
        Integer integer = stockWorklistService.feignChangeStockWorkStatus(orderId);
        return integer;
    }

}

