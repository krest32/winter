package com.krest.blog.controller;


import com.krest.blog.entity.Tag;
import com.krest.blog.service.TagService;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
@CrossOrigin
@Api(value = "标签管理",tags ="标签管理")
@RestController
@RequestMapping("/blog/tag")
public class TagController {

    public static IdWorker idWorker = new IdWorker();

    @Autowired
    private TagService tagService;

    @GetMapping("listAllTag")
    @ApiOperation(value = "得到所有的Tag")
    public R listAllTag(){
        List<Tag> list = tagService.listAllTag(null);
        return R.ok().data("tagList",list);
    }

    @ApiOperation(value = "添加新的Tag信息")
    @PostMapping("addTag")
    public R addTag(@RequestBody Tag tag){
        tag.setId(idWorker.nextId());
        tagService.save(tag);
        return R.ok();
    }

    @ApiOperation(value = "通过Id查找Tag")
    @GetMapping("getTagByID/{id}")
    public R getTagByID(@PathVariable String id){
        Tag tag = tagService.getById(id);
        return R.ok().data("tag",tag);
    }

    @ApiOperation(value = "跟新Tag信息")
    @PostMapping("updateTag")
    public R updateTag(@RequestBody Tag tag){
        tagService.updateById(tag);
        return R.ok();
    }

    @ApiOperation(value = "删除Tag")
    @DeleteMapping("deleteTag/{id}")
    public R deleteTag(@PathVariable String id){
        Integer number = tagService.judge(id);
        if( number == 0 ){
            tagService.removeById(id);
            return R.ok();
        }else{
            return R.error().message("有Blog正在使用该Tag，无法删除");
        }
    }




}

