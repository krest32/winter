package com.krest.product.entity.vo;

import com.krest.product.entity.Stock;
import lombok.Data;

import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/12/14 15:00
 * @Description:
 */
@Data
public class ResultStockVo {

    private Long total=0L;
    private List<Stock> stockList;

}
