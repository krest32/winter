package com.krest.blog.controller;


import com.krest.blog.entity.Blog;
import com.krest.blog.entity.vo.QueryBlogVo;
import com.krest.blog.service.BlogService;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 博客表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
@CrossOrigin
@Api(value = "博客管理",tags ="博客管理")
@RestController
@RequestMapping("/blog/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @ApiOperation(value = "条件分页条件查询Blog")
    @PostMapping("PageQueryBlog/{page}/{limit}")
    public R PageQueryBlog(@PathVariable Long page,
                           @PathVariable Long limit,
                           @RequestBody(required = false) QueryBlogVo queryBlogVo){
        return blogService.PageQueryBlog(page,limit,queryBlogVo);
    }

    @ApiOperation(value = "添加新的Blog")
    @PostMapping("addBlog")
    public R addBlog(@RequestBody Blog blog){
        blogService.addBlog(blog);
        return R.ok();
    }

    @ApiOperation(value = "改变Blog的状态")
    @GetMapping("changeStatus/{id}")
    public R changeStatus(@PathVariable String id){
        blogService.changeStatus(id);
        return R.ok();
    }

    @ApiOperation(value = "根据Id删除Blog")
    @DeleteMapping("deleteBlog/{id}")
    public R deleteBlog(@PathVariable String id){
        blogService.deleteBlog(id);
        return R.ok();
    }

    @ApiOperation(value = "根据Id得到Blog")
    @GetMapping("getBlogById/{id}")
    public R getBlogById(@PathVariable String id){
        Blog blog = blogService.getBlogById(id);
        return R.ok().data("blog",blog);
    }

    @ApiOperation(value = "更新Blog信息")
    @PostMapping("updateBlog")
    public R updateBlog(@RequestBody Blog blog){
        blogService.updateBlog(blog);
        return R.ok();
    }

    @ApiOperation(value = "WEB: 根据AuthorId得到Blog")
    @GetMapping("getBlogByAuthor/{id}/{page}/{limit}")
    public R getBlogByAuthor(@PathVariable String id,
                             @PathVariable Long page,
                             @PathVariable Long limit){
        return blogService.getBlogByAuthor(id,page,limit);

    }


    @ApiOperation(value = "WEB: 列出热门的Blog")
    @GetMapping("getHotBlog")
    public R getHotBlog(){
        List<Blog> blogList = blogService.getHotBlog();
        return R.ok().data("blogList",blogList);
    }

    @ApiOperation(value = "根据Id得到Blog")
    @GetMapping("webGetBlogById/{id}")
    public R WebGetBlogById(@PathVariable String id){
        Blog blog = blogService.WebGetBlogById(id);
        return R.ok().data("blog",blog);
    }
}

