package com.krest.product.controller;


import com.krest.product.entity.CatelogAttr;
import com.krest.product.entity.CatelogAttrgroup;
import com.krest.product.service.CatelogAttrService;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 属性描述表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Api(value = "分类属性",tags ="分类属性")
@CrossOrigin
@RestController
@RequestMapping("/product/catelog-attr")
public class CatelogAttrController {

    @Autowired
    private CatelogAttrService catelogAttrService;

    @ApiOperation(value = "得到属性列表")
    @GetMapping("getAttrByGroupId/{groupId}")
    public R getAttrByGroupId(@PathVariable String groupId){
        List<CatelogAttr> attrList = catelogAttrService.getAttrByGroupId(groupId);
        return R.ok().data("attrList",attrList);
    }

    @ApiOperation(value = "添加新的Attr")
    @PostMapping("addNewAttr")
    public R addNewAttr(@RequestBody CatelogAttr catelogAttr){
        catelogAttrService.addNewAttr(catelogAttr);
        return R.ok();
    }

    @ApiOperation(value = "根据Id删除属性")
    @DeleteMapping("deleteAttrById/{id}")
    public R deleteAttrById(@PathVariable String id){
        catelogAttrService.deleteAttrById(id);
        return R.ok();
    }

    @ApiOperation(value = "根据Id得到Attr")
    @GetMapping("getAttrById/{id}")
    public R getAttrById(@PathVariable String id){
        CatelogAttr catelogAttr = catelogAttrService.getById(id);
        return R.ok().data("attr",catelogAttr);
    }

    @ApiOperation(value = "修改已有的Attr")
    @PostMapping("updateAttr")
    public R updateAttr(@RequestBody CatelogAttr catelogAttr){
        catelogAttrService.updateAttr(catelogAttr);

        return R.ok();
    }




}

