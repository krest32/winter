package com.krest.product.entity.vo;

import com.krest.product.entity.Product;
import lombok.Data;

import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/12/13 14:47
 * @Description:
 */
@Data
public class ResultQueryProductVo {
    private Long total;
    private List<Product> productList;
}
