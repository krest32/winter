package com.krest.vedio.controller;


import com.krest.utils.response.R;
import com.krest.vedio.entity.Catelog;
import com.krest.vedio.service.CatelogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 视频分类表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-05
 */
@CrossOrigin
@Api(value = "视频分类",tags ="视频分类")
@RestController
@RequestMapping("/vedio/catelog")
public class CatelogController {

    @Autowired
    private CatelogService catelogService;

    @ApiOperation(value = "添加新的Catelog")
    @PostMapping("addCateLog")
    public R addCateLog(@RequestBody Catelog catelog){
        catelogService.addCateLog(catelog);
        return R.ok();
    }

    @ApiOperation(value = "列出所有的Catelog")
    @GetMapping("listCateLog")
    public R listCateLog(){
        List<Catelog> list = catelogService.listCateLog();
        return R.ok().data("list",list);
    }


    @ApiOperation(value = "根据id得到Catelog")
    @GetMapping("getCatelogById/{id}")
    public R getCatelogById(@PathVariable String id){
        Catelog catelog = catelogService.getCatelogById(id);
        return R.ok().data("catelog",catelog);
    }

    @ApiOperation(value = "更新Catelog信息")
    @PostMapping("updateCatelog")
    public R updateCatelog(@RequestBody Catelog catelog){
        catelogService.updateById(catelog);
        return R.ok();
    }

    @ApiOperation(value = "删除Catelog")
    @DeleteMapping("deleteCatelog/{id}")
    public R deleteCatelog(@PathVariable String id){
        catelogService.deleteCatelog(id);
        return R.ok();
    }

}

