package com.krest.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krest.product.client.AliyunClient;
import com.krest.product.entity.Brand;
import com.krest.product.entity.Product;
import com.krest.product.entity.vo.QueryBrandVo;
import com.krest.product.mapper.BrandMapper;
import com.krest.product.service.BrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.product.service.ProductService;
import com.krest.utils.myexception.myException;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 品牌描述 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Autowired
    private ProductService productService;

    @Autowired
    private AliyunClient aliyunClient;

    public static IdWorker idWorker = new IdWorker();

    @Override
    public List<Brand> ListAllBrand() {
        List<Brand> brandList = baseMapper.selectList(null);
        List<Brand> collect = brandList.stream().map(brand -> {
            return editBrandInfo(brand);
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Brand getBrandById(String id) {
        Brand brand = baseMapper.selectById(id);
        Brand brandInfo = editBrandInfo(brand);
        return brandInfo;
    }

    @Override
    public void DeleteBrandById(String id) {
        Brand brand = baseMapper.selectById(id);
        Brand brandInfo = editBrandInfo(brand);
        Integer productNum = brandInfo.getProductNum();
        if (productNum>0){
            throw new myException(20001,"无法删除：产品数量不为0");
        }
        aliyunClient.deleteOss(brand.getBrandImageFilename());
        baseMapper.deleteById(id);
    }

    @Override
    public void AddNewBrand(Brand brand) {
        brand.setId(idWorker.nextId());
        String brandImageFilename = brand.getBrandImageFilename();
        if (StringUtils.isEmpty(brandImageFilename)){
            throw new myException(20001,"请上传图片");
        }
        baseMapper.insert(brand);
    }

    @Override
    public void updateBrand(Brand brand) {
        Brand byId = baseMapper.selectById(brand.getId());
        if(byId.getBrandImageFilename().equals(brand.getBrandImageFilename())){
            baseMapper.updateById(brand);
        }else{
            aliyunClient.deleteOss(byId.getBrandImageFilename());
            baseMapper.updateById(brand);
        }
    }

    @Override
    public R pageQueryBrand(Long page, Long limit, QueryBrandVo queryBrandVo) {
        String title = queryBrandVo.getTitle();
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        Page<Brand> brandPage = new Page<>(page,limit);

        if (!StringUtils.isEmpty(title)){
            queryWrapper.like("title",title);
        }

        IPage<Brand> brandIPage = baseMapper.selectPage(brandPage, queryWrapper);
        List<Brand> records = brandIPage.getRecords();
        Long total = brandIPage.getTotal();

        List<Brand> collect = records.stream().map(brand -> {
            return editBrandInfo(brand);
        }).collect(Collectors.toList());


        return R.ok().data("total",total).data("brandList",collect);

    }

    private Brand editBrandInfo(Brand brand) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("brand_id",brand.getId());
        int count = productService.count(queryWrapper);
        brand.setProductNum(count);
        return brand;
    }
}
