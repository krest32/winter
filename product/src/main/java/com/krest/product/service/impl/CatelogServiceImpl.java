package com.krest.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krest.product.entity.Catelog;
import com.krest.product.entity.Product;
import com.krest.product.mapper.CatelogMapper;
import com.krest.product.service.CatelogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 三级分类关系表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-12
 */
@Service
public class CatelogServiceImpl extends ServiceImpl<CatelogMapper, Catelog> implements CatelogService {

    @Autowired
    private ProductService productService;

    @Override
    public List<Catelog> GetAllCatelog() {
        List<Catelog> catelogs = baseMapper.selectList(null);

        for (Catelog catelog : catelogs) {
            String id = catelog.getId();

            QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
            productQueryWrapper.eq("one_sort_id",id);

            int count =  productService.count(productQueryWrapper);
            catelog.setProductNum(count);

        }



        List<Catelog> collect = catelogs.stream().filter(cateLog ->
                cateLog.getParentId().equals("0"
                )).map((cateLog)->{
            return cateLog.setChildrenCatelogs(getchildCatelog(cateLog,catelogs));
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<Catelog> listCateLogThree() {
        QueryWrapper<Catelog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id","0");
        List<Catelog> catelogSorts = baseMapper.selectList(queryWrapper);
        return catelogSorts;
    }

    // 递归获取子菜单
    private List<Catelog> getchildCatelog(Catelog cateLog, List<Catelog> catelogs) {
        List<Catelog> child = catelogs.stream().filter(cateLogchild -> {
            return cateLogchild.getParentId().equals(cateLog.getId());
        }).map((cateLogchild) -> {
            return cateLogchild.setChildrenCatelogs(getchildCatelog(cateLogchild, catelogs));
        }).collect(Collectors.toList());
        return child;
    }
}
