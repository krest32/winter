package com.krest.others.controller;


import com.krest.others.entity.SearchVo;
import com.krest.others.service.EsService;
import com.krest.utils.entity.EsProductModel;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @Auther: krest
 * @Date: 2020/11/13 11:48
 * @Description:
 */

@CrossOrigin
@Api(value = "ES 搜素引擎",tags ="ES 搜素引擎")
@RestController
@RequestMapping("/others/es")
public class EsController {

    @Autowired
    private EsService esService;


    @ApiOperation(value = "商品上架功能")
    @PostMapping("saveEsProduct")
    public R saveProduct(@RequestBody EsProductModel esProductModel) throws IOException {
        return  esService.saveProduct(esProductModel);
    }

    @ApiOperation(value = "商品下架功能")
    @DeleteMapping("deleteEsProduct/{id}")
    public R deleteEsProduct(@PathVariable String id) throws IOException {
        return  esService.deleteProduct(id);
    }

    @ApiOperation("根据条件查询对应的Product")
    @PostMapping("searchProduct/{page}/{limit}")
    public R searchProduct(@PathVariable Long page,
                           @PathVariable Long limit,
                           @RequestBody(required = false) SearchVo searchVo) throws IOException {
        return esService.searchProduct(page,limit,searchVo);
    }

}
