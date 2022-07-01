package com.krest.product.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/11/17 16:12
 * @Description:
 */
@Data
public class SaveBrandSortvo {
    private String sortId;
    private List<String> brandIdList;
}
