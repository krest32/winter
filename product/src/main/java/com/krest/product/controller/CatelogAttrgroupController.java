package com.krest.product.controller;


import com.krest.product.entity.CatelogAttrgroup;
import com.krest.product.service.CatelogAttrgroupService;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 分类属性分组表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */

@Api(value = "属性分组",tags ="属性分组")
@CrossOrigin
@RestController
@RequestMapping("/product/catelog-attrgroup")
public class CatelogAttrgroupController {

    @Autowired
    private CatelogAttrgroupService catelogAttrgroupService;

    @ApiOperation(value = "根据ID得到分类属性组列表")
    @GetMapping("GetAttrGroupById/{id}")
    private R GetAttrGroupListById(@PathVariable String id){
        List<CatelogAttrgroup> attrGopurList= catelogAttrgroupService.GetAttrGroupById(id);
        return R.ok().data("attrGopurList",attrGopurList);
    }

    @ApiOperation(value = "添加新分组属性")
    @PostMapping("addNewAttrGroup")
    public R addNewAttrGroup(@RequestBody CatelogAttrgroup catelogAttrgroup){
        catelogAttrgroupService.addNewAttrGroup(catelogAttrgroup);
        return R.ok();
    }

    @ApiOperation(value = "根据ID删除属性组")
    @DeleteMapping("deleteAttrGroup/{id}")
    public R deleteAttrGroup(@PathVariable String id){
        return catelogAttrgroupService.deleteAttrGroup(id);
    }

    @ApiOperation(value = "根据id得到Group信息")
    @GetMapping("getAttrGroupInfo/{id}")
    public R getAttrGroupInfo(@PathVariable String id){
        CatelogAttrgroup byId = catelogAttrgroupService.getById(id);
        return R.ok().data("attrGroupInfo",byId);
    }

    @ApiOperation(value = "更新分组属性信息")
    @PostMapping("updateNewAttrGroup")
    public R updateNewAttrGroup(@RequestBody CatelogAttrgroup catelogAttrgroup){
        if(catelogAttrgroup.getId()==null){
            return R.error().message("更新内容没有主键ID");
        }
        catelogAttrgroupService.updateById(catelogAttrgroup);
        return R.ok();
    }

}

