package com.krest.product.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krest.product.entity.Brand;
import com.krest.product.entity.Product;
import com.krest.product.entity.vo.QueryBrandVo;
import com.krest.product.service.BrandService;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 品牌描述 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Api(value = "品牌管理",tags ="品牌管理")
@CrossOrigin
@RestController
@RequestMapping("/product/brand")
public class BrandController {

    public static IdWorker idWorker = new IdWorker();

    @Autowired
    private BrandService brandService;

    @ApiOperation(value = "列出所有品牌信息")
    @GetMapping("ListAllBrand")
    public R ListAllBrand(){
        List<Brand> list = brandService.ListAllBrand();
        return R.ok().data("list",list);
    }

    @ApiOperation(value = "列出所有品牌信息")
    @PostMapping("pageQueryBrand/{page}/{limit}")
    public R pageQueryBrand(@PathVariable Long page,
                            @PathVariable Long limit,
                            @RequestBody(required = false) QueryBrandVo queryBrandVo){
        return  brandService.pageQueryBrand(page,limit,queryBrandVo);
    }

    @ApiOperation(value = "删除品牌信息")
    @DeleteMapping("DeleteBrandById/{id}")
    public R DeleteBrandById(@PathVariable String id){
        brandService.DeleteBrandById(id);
        return R.ok();
    }

    @ApiOperation(value = "新增品牌信息")
    @PostMapping("AddNewBrand")
    public R AddNewBrand(@RequestBody Brand brand){
        brandService.AddNewBrand(brand);
        return R.ok();
    }


    @ApiOperation(value = "根据id得到品牌信息")
    @GetMapping("getBrandById/{id}")
    public R getBrandById(@PathVariable String id){
        Brand brand = brandService.getBrandById(id);
        return R.ok().data("brand",brand);
    }


    @ApiOperation(value = "修改品牌信息")
    @PostMapping("updateBrand")
    public R updateBrand(@RequestBody Brand brand){
        brandService.updateBrand(brand);
        return R.ok();
    }



}

