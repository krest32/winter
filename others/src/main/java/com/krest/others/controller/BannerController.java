package com.krest.others.controller;


import com.krest.others.entity.Banner;
import com.krest.others.service.BannerService;
import com.krest.others.service.OthersService;
import com.krest.utils.myexception.myException;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-06
 */
@Api(value = "阿里云服务",tags ="阿里云服务")
@CrossOrigin
@RestController
@RequestMapping("/others/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @Autowired
    private OthersService othersService;

    public static IdWorker idWorker = new IdWorker();

    @ApiOperation(value = "列出所有的Banner" )
    @GetMapping("listBanner")
    public R listBanner(){
        List<Banner> list = bannerService.list(null);
        return R.ok().data("bannerList",list);
    }


    @ApiOperation(value = "根据Id得到banner" )
    @GetMapping("getBannerById/{id}")
    public R getBannerById(@PathVariable String id){
        Banner banner= bannerService.getById(id);
        return R.ok().data("banner",banner);
    }


    @ApiOperation(value = "添加新的banner" )
    @PostMapping("addBanner")
    public R addBanner(@RequestBody Banner banner){
        banner.setId(idWorker.nextId());
        String imageFilename = banner.getImageFilename();
        if(StringUtils.isEmpty(imageFilename)){
            throw new myException(20001,"默认图片无法使用，请重新上传图片");
        }else {
            bannerService.save(banner);
        }
        return R.ok();
    }


    @ApiOperation(value = "删除指定banner" )
    @DeleteMapping("deleteBanner/{id}")
    public R deleteBanner(@PathVariable String id){
        Banner banner = bannerService.getById(id);

        String imageFilename = banner.getImageFilename();
        othersService.deleteOss(imageFilename);

        bannerService.removeById(id);

        return R.ok();
    }


    @ApiOperation(value = "更新banner信息" )
    @PostMapping("updateBanner")
    public R updateBanner(@RequestBody Banner banner){
        String bannerId = banner.getId();
        Banner bannerServiceById = bannerService.getById(bannerId);

        String imageFilename = bannerServiceById.getImageFilename();
        String filename = banner.getImageFilename();

        if (!imageFilename.equals(filename)){
            othersService.deleteOss(imageFilename);
        }

        bannerService.updateById(banner);

        return R.ok();
    }


}

