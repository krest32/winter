package com.krest.product.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krest.product.entity.Catelog;
import com.krest.product.service.CatelogService;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 三级分类关系表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-12
 */
@Api(value = "产品分类",tags ="产品分类")
@CrossOrigin
@RestController
@RequestMapping("/product/catelog")
public class CatelogController {

    @Autowired
    private CatelogService catelogService;

    public static IdWorker idWorker = new IdWorker();

    @ApiOperation(value = "得到所有分类")
    @GetMapping("GetALlCateLog")
    public R GetAllCatelog(){
        List<Catelog> catelogList = catelogService.GetAllCatelog();
        return R.ok().data("catelogList",catelogList);
    }


    @ApiOperation(value = "删除分类接口")
    @DeleteMapping("deleteCateLog/{id}")
    public R deleteCatelog(@PathVariable String id){
        catelogService.removeById(id);
        return R.ok();
    }

    @ApiOperation(value = "添加分类")
    @PostMapping("AddNewCateLog")
    public R AddNewCateLog(@RequestBody Catelog catelog){
        catelog.setId(idWorker.nextId());
        catelogService.save(catelog);
        return R.ok();
    }

    @ApiOperation(value = "根据Id得到分类信息")
    @GetMapping("GetCateLogByID/{id}")
    public R GetCateLogByID(@PathVariable String id){
        Catelog catelog = catelogService.getById(id);
        return R.ok().data("catelog",catelog);
    }

    @ApiOperation(value = "更新分类方法")
    @PostMapping("UpdateNewCateLog")
    public R UpdateNewCateLog(@RequestBody Catelog catelog){
        catelogService.updateById(catelog);
        return R.ok();
    }


    @ApiOperation(value = "添加主分类")
    @PostMapping("AddNewMainCateLog")
    public R AddNewMainCateLog(@RequestBody Catelog catelog){
        catelog.setId(idWorker.nextId());
        catelog.setParentId("0");
        catelogService.save(catelog);
        return R.ok();
    }

    @ApiOperation(value = "所有的一级分类列表")
    @GetMapping("listCateLogThree")
    public R listCateLogThree(){
        List<Catelog> listCateLogThree = catelogService.listCateLogThree();
        return R.ok().data("listCateLogThree",listCateLogThree);
    }

    @ApiOperation(value = "根据Id查找子分类列表")
    @GetMapping("ListChildCatelog/{id}")
    public R ListChildCatelog(@PathVariable String id){
        QueryWrapper<Catelog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        List<Catelog> ListChildCatelog = catelogService.list(queryWrapper);
        return R.ok().data("ListChildCatelog",ListChildCatelog);
    }


}

