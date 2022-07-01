package com.krest.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krest.product.entity.CatelogAttr;
import com.krest.product.entity.CatelogAttrgroup;
import com.krest.product.mapper.CatelogAttrgroupMapper;
import com.krest.product.service.CatelogAttrService;
import com.krest.product.service.CatelogAttrgroupService;
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
 * 分类属性分组表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Service
public class CatelogAttrgroupServiceImpl extends ServiceImpl<CatelogAttrgroupMapper, CatelogAttrgroup> implements CatelogAttrgroupService {

    public static IdWorker idWorker = new IdWorker();

    @Autowired
    private CatelogAttrService catelogAttrService;

    @Override
    public List<CatelogAttrgroup> GetAttrGroupById(String id) {
        QueryWrapper<CatelogAttrgroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("one_sort_id",id);
        List<CatelogAttrgroup> CatelogAttrgroups = baseMapper.selectList(queryWrapper);
        List<CatelogAttrgroup> collect = CatelogAttrgroups.stream().map(catelogAttrgroup -> {
            return editAttrGroup(catelogAttrgroup);
        }).collect(Collectors.toList());
        return collect;
    }



    @Override
    public void addNewAttrGroup(CatelogAttrgroup catelogAttrgroup) {
        if(catelogAttrgroup.getOneSortId().isEmpty()){
            throw new myException(20001,"请添加一级分类");
        }
        catelogAttrgroup.setId(idWorker.nextId());
        baseMapper.insert(catelogAttrgroup);
    }

    @Override
    public R deleteAttrGroup(String id) {
        QueryWrapper<CatelogAttr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_group_id",id);
        Integer sum = catelogAttrService.count(queryWrapper);
        if (sum>0){
            return R.error().message("该分组属性正在使用，无法删除");
        }else{
            baseMapper.deleteById(id);
            return R.ok();
        }
    }

    private CatelogAttrgroup editAttrGroup(CatelogAttrgroup catelogAttrgroup) {
        QueryWrapper<CatelogAttr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_group_id", catelogAttrgroup.getId());
        //添加属性数量
        int count = catelogAttrService.count(queryWrapper);
        catelogAttrgroup.setAttrNum(count);

        // 添加属性详细信息
        List<CatelogAttr> list = catelogAttrService.list(queryWrapper);
        catelogAttrgroup.setAttrList(list);

        return catelogAttrgroup;
    }

}
