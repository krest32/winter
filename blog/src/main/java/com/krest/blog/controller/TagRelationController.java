package com.krest.blog.controller;


import com.krest.blog.service.TagRelationService;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 博客标签关系表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
@CrossOrigin
@Api(value = "博客标签关系管理",tags ="博客标签关系管理")
@RestController
@RequestMapping("/blog/tag-relation")
public class TagRelationController {

    @Autowired
    private TagRelationService tagRelationService;

    @ApiOperation("根据tagId分页查询Blog")
    @GetMapping("queryBlogListByTagId/{tagId}")
    public R queryBlogListByTagId(@PathVariable String tagId){
        return tagRelationService.queryBlogListByTagId(tagId);
    }
}

