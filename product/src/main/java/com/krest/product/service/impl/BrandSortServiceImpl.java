package com.krest.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krest.product.entity.Brand;
import com.krest.product.entity.BrandSort;
import com.krest.product.entity.Catelog;
import com.krest.product.entity.Product;
import com.krest.product.entity.vo.BrandSortVo;
import com.krest.product.entity.vo.SaveBrandSortvo;
import com.krest.product.mapper.BrandSortMapper;
import com.krest.product.service.BrandService;
import com.krest.product.service.BrandSortService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.product.service.CatelogService;
import com.krest.product.service.ProductService;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 品牌分类关系表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Service
public class BrandSortServiceImpl extends ServiceImpl<BrandSortMapper, BrandSort> implements BrandSortService {

    public static IdWorker idWorker = new IdWorker();

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CatelogService catelogService;

    @Override
    public BrandSortVo getBrandBySortId(String sortId) {
        QueryWrapper<BrandSort> brandSortQueryWrapper = new QueryWrapper<>();
        brandSortQueryWrapper.eq("one_sort_id",sortId);
        List<BrandSort> brandSorts = baseMapper.selectList(brandSortQueryWrapper);

        List<String> brandIds = new ArrayList<>();

        if (brandSorts.size()==0){
            BrandSortVo brandSortVo = new BrandSortVo();
            Catelog catelog = catelogService.getById(sortId);
            brandSortVo.setSortId(sortId);
            brandSortVo.setBrandIdList(brandIds);
            brandSortVo.setSortTitle(catelog.getTitle());
            return brandSortVo;

        }else{
            for (BrandSort brandSort : brandSorts) {
                String brandId = brandSort.getBrandId();
                brandIds.add(brandId);
            }

            List<Brand> brands = (List) brandService.listByIds(brandIds);
            Catelog catelog = catelogService.getById(sortId);
            List<Brand> brandsList = brands.stream().map(brand -> {
                return editBrandInfo(brand);
            }).collect(Collectors.toList());

            List<String> idList = new ArrayList<>();
            for (Brand brand : brandsList) {
                String id = brand.getId();
                idList.add(id);
            }


            BrandSortVo brandSortVo = new BrandSortVo();
            brandSortVo.setSortId(sortId);
            brandSortVo.setBrandIdList(idList);
            brandSortVo.setSortTitle(catelog.getTitle());
            brandSortVo.setBrandList(brandsList);

            return brandSortVo;
        }
    }

    @Override
    public R saveBrandVo(SaveBrandSortvo saveBrandSortvo) {
        String sortId = saveBrandSortvo.getSortId();

        //先删除之前的数据
        QueryWrapper<BrandSort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("one_sort_id",sortId);
        baseMapper.delete(queryWrapper);

        //重新进行添加
        List<String> brandIdList = saveBrandSortvo.getBrandIdList();
        if (sortId.equals(null)&&brandIdList.size()<=0){
            return R.error().message("关联数据出错");
        }

        for (String brandId : brandIdList) {
            BrandSort brandSort = new BrandSort();
            brandSort.setId(idWorker.nextId());
            brandSort.setBrandId(brandId);
            brandSort.setOneSortId(sortId);
            baseMapper.insert(brandSort);
        }
        return R.ok();
    }

    @Override
    public R cancelRelation(String sortId, String brandId) {
        //1. 根据Id查找关联表中信息
        QueryWrapper<BrandSort> queryWrapper = new QueryWrapper<>();
        if (sortId != null && brandId != null) {
            queryWrapper.eq("one_sort_id", sortId);
            queryWrapper.eq("brand_id", brandId);
            List<BrandSort> brandCatelogs = baseMapper.selectList(queryWrapper);
            if (brandCatelogs.size() == 1) {
                baseMapper.deleteById(brandCatelogs.get(0).getId());
                return R.ok().message("成功解除关联");
            } else {
                return R.error().message("解除关联出现异常");
            }
        }
        return null;
    }

    private Brand editBrandInfo(Brand brand) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("brand_id",brand.getId());
        int count = productService.count(queryWrapper);
        brand.setProductNum(count);
        return brand;
    }


}
