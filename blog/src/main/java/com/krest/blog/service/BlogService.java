package com.krest.blog.service;

import com.krest.blog.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krest.blog.entity.Sort;
import com.krest.blog.entity.vo.BlogInfo;
import com.krest.blog.entity.vo.QueryBlogVo;
import com.krest.utils.response.R;

import java.util.List;

/**
 * <p>
 * 博客表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
public interface BlogService extends IService<Blog> {

    R PageQueryBlog(Long page, Long limit, QueryBlogVo queryBlogVo);

    void addBlog(Blog blog);

    void changeStatus(String id);

    void deleteBlog(String id);

    Blog getBlogById(String id);

    void updateBlog(Blog blog);

    R getBlogByAuthor(String id,Long page,Long limit);

    List<Blog> getHotBlog();

    Blog WebGetBlogById(String id);
}
