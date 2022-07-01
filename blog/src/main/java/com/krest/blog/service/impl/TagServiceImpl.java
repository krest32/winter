package com.krest.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krest.blog.entity.Tag;
import com.krest.blog.entity.TagRelation;
import com.krest.blog.mapper.TagMapper;
import com.krest.blog.service.TagRelationService;
import com.krest.blog.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagRelationService tagRelationService;

    @Override
    public Integer judge(String id) {
        QueryWrapper<TagRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_id",id);
        List<TagRelation> list = tagRelationService.list(queryWrapper);
        int size = list.size();
        return size;
    }

    @Override
    public List<Tag> listAllTag(Object o) {
        List<Tag> tagList = baseMapper.selectList(null);
        List<Tag> collect = tagList.stream().map(tag -> {
            return getTagUseNum(tag);
        }).collect(Collectors.toList());
        return collect;
    }

    private Tag getTagUseNum(Tag tag) {
        String tagId = tag.getId();
        QueryWrapper<TagRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_id",tagId);
        int count = tagRelationService.count(queryWrapper);
        tag.setTagUseNum(count);
        return tag;
    }
}
