package com.krest.product.entity.vo;

import com.krest.product.entity.CatelogAttr;
import com.krest.product.entity.CatelogAttrgroup;
import lombok.Data;

import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/12/13 19:14
 * @Description:
 */
@Data
public class SaveProductAttrVo {
    private String productId;
    private List<CatelogAttr> catelogAttrList;
}
