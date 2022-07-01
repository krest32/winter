package com.krest.product.service;

import com.krest.product.entity.Brand;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krest.product.entity.vo.QueryBrandVo;
import com.krest.utils.response.R;

import java.util.List;

/**
 * <p>
 * 品牌描述 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
public interface BrandService extends IService<Brand> {

    List<Brand> ListAllBrand();

    Brand getBrandById(String id);

    void DeleteBrandById(String id);

    void AddNewBrand(Brand brand);

    void updateBrand(Brand brand);

    R pageQueryBrand(Long page, Long limit, QueryBrandVo queryBrandVo);
}
