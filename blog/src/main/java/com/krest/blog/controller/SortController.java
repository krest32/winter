package com.krest.blog.controller;


import com.krest.blog.entity.Author;
import com.krest.blog.entity.Sort;
import com.krest.blog.entity.vo.QueryAuthorVo;
import com.krest.blog.entity.vo.QuerySortVo;
import com.krest.blog.service.SortService;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * blog分类表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
@CrossOrigin
@Api(value = "博客分类管理",tags ="博客分类管理")
@RestController
@RequestMapping("/blog/sort")
public class SortController {
    @Autowired
    private SortService sortService;

    @ApiOperation(value = "添加新的Sort信息")
    @PostMapping("addSort")
    public R addSort(@RequestBody Sort sort){
        sortService.addSort(sort);
        return R.ok();
    }

    @ApiOperation(value = "条件分页条件查询Sort")
    @PostMapping("PageQuerySort/{page}/{limit}")
    public R PageQuerySort(@PathVariable Long page,
                             @PathVariable Long limit,
                             @RequestBody(required = false) QuerySortVo querySortVo){
        return sortService.PageQuerySort(page,limit,querySortVo);
    }

    @ApiOperation(value = "根据Id得到Sort信息")
    @GetMapping("getSortById/{id}")
    public R getSortById(@PathVariable String id){
        Sort sort = sortService.getSortById(id);
        return R.ok().data("sort",sort);
    }

    @ApiOperation(value = "根据Id更新Sort信息")
    @PostMapping("updateSort")
    public R updateSort(@RequestBody Sort sort){
        sortService.updateSort(sort);
        return R.ok();
    }

    @ApiOperation(value = "根据Id更新Sort信息")
    @DeleteMapping("deleteSort/{id}")
    public R deleteSort(@PathVariable String id){
        sortService.deleteSort(id);
        return R.ok();
    }

    @ApiOperation(value = "获取Sort下拉列表")
    @GetMapping("listAllSort")
    public R listAllSort(){
        List<Sort> list = sortService.list(null);
        return R.ok().data("sortList",list);
    }


}

