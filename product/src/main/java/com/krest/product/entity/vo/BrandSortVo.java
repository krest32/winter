package com.krest.product.entity.vo;

import com.krest.product.entity.Brand;
import lombok.Data;

import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/11/17 13:46
 * @Description:
 */
@Data
public class BrandSortVo {

    private String sortId;
    private String sortTitle;
    private List<String> brandIdList;
    private List<Brand> brandList;
}
