package com.krest.product.controller;


import com.krest.product.entity.Product;
import com.krest.product.entity.vo.SaveProductAttrVo;
import com.krest.product.service.AttrService;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 商品属性描述 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Api(value = "产品属性",tags ="产品属性")
@CrossOrigin
@RestController
@RequestMapping("/product/attr")
public class AttrController {

    @Autowired
    private AttrService attrService;

    @ApiOperation(value="为商品添加属性")
    @PostMapping("addAttrForProduct")
    public R addAttrForProduct(@RequestBody SaveProductAttrVo saveProductAttrVo){
        attrService.addAttrForProduct(saveProductAttrVo);
        return R.ok();
    }

    @ApiOperation(value="为商品删除属性")
    @DeleteMapping("deleteProductAttr/{id}")
    public R deleteProductAttr(@PathVariable String id){
        attrService.removeById(id);
        return R.ok();
    }


}

