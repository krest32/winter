package com.krest.product.entity.vo;

import com.krest.product.entity.Attr;
import lombok.Data;

import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/12/13 14:40
 * @Description:
 */
@Data
public class ProductAttrVo {
    private String attrGroupId;
    private String attrGrouptitle;
    private List<Attr> attrList;
}
