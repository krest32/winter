package com.krest.product.service;

import com.krest.product.entity.Attr;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krest.product.entity.Product;
import com.krest.product.entity.vo.SaveProductAttrVo;

/**
 * <p>
 * 商品属性描述 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
public interface AttrService extends IService<Attr> {

    void addAttrForProduct(SaveProductAttrVo saveProductAttrVo);
}
