package com.krest.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krest.blog.entity.Blog;
import com.krest.blog.entity.TagRelation;
import com.krest.blog.mapper.TagRelationMapper;
import com.krest.blog.service.BlogService;
import com.krest.blog.service.PictureService;
import com.krest.blog.service.TagRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.utils.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 博客标签关系表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
@Service
public class TagRelationServiceImpl extends ServiceImpl<TagRelationMapper, TagRelation> implements TagRelationService {

    @Autowired
    private BlogService blogService;

    @Autowired
    private PictureService pictureService;

    @Override
    public R queryBlogListByTagId(String tagId) {
        QueryWrapper<TagRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_id",tagId);
        List<TagRelation> tagRelations = baseMapper.selectList(queryWrapper);
        List<String> blogIdList = new ArrayList<>();
        for (TagRelation tagRelation : tagRelations) {
            blogIdList.add(tagRelation.getBlogId());
        }
        List<Blog> blogs = (List)blogService.listByIds(blogIdList);
        List<Blog> res = blogs.stream().map(blog -> {
            return blog.setPictureAddress(pictureService.getById(blog.getPictureUid()).getAddress());
        }).collect(Collectors.toList());

        return R.ok().data("blogList",res).data("total",res.size());

    }
}
