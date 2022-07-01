package com.krest.product.service;

import com.krest.product.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krest.product.entity.vo.QueryPuoductVo;
import com.krest.product.entity.vo.ResultQueryProductVo;
import com.krest.utils.response.R;

/**
 * <p>
 * 商品信息表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
public interface ProductService extends IService<Product> {

    ResultQueryProductVo pageQueryAllProduct(Long page, Long limit, QueryPuoductVo queryPuoductVo);

    void addNewProduct(Product product);

    Product getProductById(String id);

    void updateProduct(Product product);

    void deleteProductById(String id);

    R changeProductStatus(String id);

    R getEsProductInfo(String id);

    R listNewProducts();
}
