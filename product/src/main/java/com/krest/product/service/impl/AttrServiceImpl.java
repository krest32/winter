package com.krest.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krest.product.entity.Attr;
import com.krest.product.entity.CatelogAttr;
import com.krest.product.entity.Product;
import com.krest.product.entity.vo.SaveProductAttrVo;
import com.krest.product.mapper.AttrMapper;
import com.krest.product.service.AttrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.utils.utils.IdWorker;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品属性描述 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Service
public class AttrServiceImpl extends ServiceImpl<AttrMapper, Attr> implements AttrService {

    public static IdWorker idWorker = new IdWorker();

    @Override
    public void addAttrForProduct(SaveProductAttrVo saveProductAttrVo) {
        System.out.println("saveProductAttrVo:"+saveProductAttrVo);
        // 先删除之前的属性
        String productId = saveProductAttrVo.getProductId();
        QueryWrapper<Attr> attrQueryWrapper = new QueryWrapper<>();
        attrQueryWrapper.eq("product_id",productId);
        baseMapper.delete(attrQueryWrapper);

        // 然后重新添加
        List<CatelogAttr> catelogAttrList = saveProductAttrVo.getCatelogAttrList();
        for (CatelogAttr catelogAttr : catelogAttrList) {
            String attrGroupId = catelogAttr.getAttrGroupId();
            if(!StringUtils.isEmpty(attrGroupId)){
                Attr attr = new Attr();
                attr.setId(idWorker.nextId());
                attr.setAttrId(catelogAttr.getId());
                attr.setAttrGroupId(catelogAttr.getAttrGroupId());
                attr.setProductId(saveProductAttrVo.getProductId());
                baseMapper.insert(attr);
            }
        }
    }
}
