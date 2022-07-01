package com.krest.product.controller;


import com.krest.product.entity.vo.BrandSortVo;
import com.krest.product.entity.vo.SaveBrandSortvo;
import com.krest.product.service.BrandSortService;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 品牌分类关系表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Api(value = "品牌分类",tags ="品牌分类")
@CrossOrigin
@RestController
@RequestMapping("/product/brand-sort")
public class BrandSortController {

    @Autowired
    private BrandSortService brandSortService;

    @ApiOperation(value = "根据sortId得到brand")
    @GetMapping("getBrandBySortId/{sortId}")
    public R getBrandBySortId(@PathVariable String sortId){
        BrandSortVo brandSortVo = brandSortService.getBrandBySortId(sortId);
        return R.ok().data("brandSortVo",brandSortVo);
    }

    @ApiOperation(value = "添加Sort与Brand 关联信息")
    @PostMapping("addNewRelation")
    public R cancelRelation(@RequestBody SaveBrandSortvo saveBrandSortvo){
        return brandSortService.saveBrandVo(saveBrandSortvo);
    }

    @ApiOperation(value = "根据SortId与BrandId 解除关联")
    @GetMapping("cancelRelation/{SortId}/{BrandId}")
    public R cancelRelation(@PathVariable String SortId,
                            @PathVariable String BrandId){
        return brandSortService.cancelRelation(SortId,BrandId);
    }

}

