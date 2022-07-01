package com.krest.product.service;

import com.krest.product.entity.BrandSort;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krest.product.entity.vo.BrandSortVo;
import com.krest.product.entity.vo.SaveBrandSortvo;
import com.krest.utils.response.R;

/**
 * <p>
 * 品牌分类关系表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
public interface BrandSortService extends IService<BrandSort> {

    BrandSortVo getBrandBySortId(String sortId);

    R saveBrandVo(SaveBrandSortvo saveBrandSortvo);

    R cancelRelation(String sortId, String brandId);
}
