package com.krest.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krest.product.entity.Attr;
import com.krest.product.entity.CatelogAttr;
import com.krest.product.mapper.CatelogAttrMapper;
import com.krest.product.service.AttrService;
import com.krest.product.service.CatelogAttrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.utils.myexception.myException;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 属性描述表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Service
public class CatelogAttrServiceImpl extends ServiceImpl<CatelogAttrMapper, CatelogAttr> implements CatelogAttrService {

    public static IdWorker idWorker = new IdWorker();

    @Autowired
    private AttrService attrService;

    @Override
    public List<CatelogAttr> getAttrByGroupId(String groupId) {
        QueryWrapper<CatelogAttr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_group_id",groupId);
        // 查询所有分下下对应的所有属性分组
        List<CatelogAttr> attrList = baseMapper.selectList(queryWrapper);
        List<CatelogAttr> collect = attrList.stream().map(catelogAttr->{
            return editAttr(catelogAttr);
        }).collect(Collectors.toList());


        return attrList;
    }



    @Override
    public void addNewAttr(CatelogAttr catelogAttr) {
        if(catelogAttr.getAttrGroupId().isEmpty()){
            throw new myException(20001,"AttrGroup不能为空");
        }
        catelogAttr.setId(idWorker.nextId());
        baseMapper.insert(catelogAttr);
    }

    @Override
    public void deleteAttrById(String id) {
        QueryWrapper<Attr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_id",id);
        int count = attrService.count(queryWrapper);
        if (count>0){
            throw  new myException(20001,"属性被使用中，无法删除");
        }
        baseMapper.deleteById(id);

    }

    @Override
    public void updateAttr(CatelogAttr catelogAttr) {
        if(catelogAttr.getId().isEmpty()&&catelogAttr.getAttrGroupId().isEmpty()){
            throw new myException(20001,"Attr信息丢失");
        }
        baseMapper.updateById(catelogAttr);
    }

    private CatelogAttr editAttr(CatelogAttr catelogAttr) {
        QueryWrapper<Attr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_id",catelogAttr.getId());
        int count = attrService.count(queryWrapper);
        catelogAttr.setAttrUseNum(count);
        return catelogAttr;
    }


}
