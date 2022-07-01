package com.krest.blog.service;

import com.krest.blog.entity.TagRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krest.utils.response.R;

/**
 * <p>
 * 博客标签关系表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
public interface TagRelationService extends IService<TagRelation> {

    R queryBlogListByTagId(String tagId);
}
