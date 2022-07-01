package com.krest.product.controller;


import com.krest.product.entity.Product;
import com.krest.product.entity.vo.QueryPuoductVo;
import com.krest.product.entity.vo.ResultQueryProductVo;
import com.krest.product.service.ProductService;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * <p>
 * 商品信息表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Api(value = "产品信息",tags ="产品信息")
@CrossOrigin
@RestController
@RequestMapping("/product/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation(value="获取商品的价格")
    @PostMapping("getPriceById/{id}")
    public BigDecimal getPriceById(@PathVariable String id){
        return productService.getById(id).getPrice();
    }



    @ApiOperation(value="商品列表")
    @PostMapping("pageQueryAllProduct/{page}/{limit}")
    public R pageQueryAllProduct(@PathVariable Long page,
                            @PathVariable Long limit,
                            @RequestBody(required = false) QueryPuoductVo queryPuoductVo){
        ResultQueryProductVo resultQueryProductVo = productService.pageQueryAllProduct(page,limit,queryPuoductVo);
        return R.ok().data("productVo",resultQueryProductVo);
    }

    @ApiOperation(value="添加商品")
    @PostMapping("addNewProduct")
    public R addNewProduct(@RequestBody Product product){
        productService.addNewProduct(product);
        return R.ok();
    }

    @ApiOperation(value="根据产品Id得到产品信息")
    @GetMapping("getProductById/{id}")
    public R getProductById(@PathVariable String id){
        Product product =  productService.getProductById(id);
        return R.ok().data("product",product);
    }

    @ApiOperation(value="根据产品Id得到产品信息")
    @GetMapping("getOrderProductById/{id}")
    public Product getOrderProductById(@PathVariable String id){
        Product product =  productService.getProductById(id);
        return product;
    }

    @ApiOperation(value="更新产品信息")
    @PostMapping("updateProduct")
    public R updateProduct(@RequestBody Product product){
        productService.updateProduct(product);
        return R.ok();
    }

    @ApiOperation(value="删除商品")
    @DeleteMapping("deleteProductById/{id}")
    public R deleteProductById(@PathVariable String id){
        productService.deleteProductById(id);
        return R.ok();
    }

    @ApiOperation(value = "根据Id改变商品的上下架状态")
    @GetMapping("changeProductStatus/{id}")
    public R changeProductStatus(@PathVariable String id){
        return  productService.changeProductStatus(id);
    }

    @ApiOperation(value = "根据Id返回Es上架需要的数据格式")
    @GetMapping("GetEsProductInfo/{id}")
    public R GetEsProductInfo(@PathVariable String id){
        return productService.getEsProductInfo(id);
    }


    @ApiOperation(value = "前端首页显示最新商品")
    @GetMapping("listNewProducts")
    public R listNewProducts(){
        return productService.listNewProducts();
    }
}

