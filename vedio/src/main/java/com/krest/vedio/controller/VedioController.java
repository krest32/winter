package com.krest.vedio.controller;


import com.krest.member.entity.Vo.QueryVedio;
import com.krest.utils.response.R;
import com.krest.vedio.client.AliyunClient;
import com.krest.vedio.entity.Catelog;
import com.krest.vedio.entity.Vedio;
import com.krest.vedio.entity.vo.QueryVedioVo;
import com.krest.vedio.service.VedioService;
import com.netflix.client.ClientException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 视频信息表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-05
 */
@CrossOrigin
@Api(value = "视频管理",tags ="视频管理")
@RestController
@RequestMapping("/vedio/vedio")
public class VedioController {

    @Autowired
    private VedioService vedioService;

    @Autowired
    private AliyunClient aliyunClient;

    @ApiOperation(value = "列出所有的Vedio")
    @PostMapping("querySearchVedio/{page}/{limit}")
    public R querySearchVedio(@PathVariable Long page,
                              @PathVariable Long limit,
                              @RequestBody(required = false) QueryVedioVo queryVedioVo){
        return vedioService.querySearchVedio(page,limit,queryVedioVo);
    }


    @ApiOperation(value = "根据id得到vedio")
    @GetMapping("getVedio/{id}")
    public R getVedio(@PathVariable String id){
        Vedio vedio = vedioService.getVedio(id);
        return R.ok().data("vedio",vedio);
    }


    @ApiOperation(value = "添加新的vedio")
    @PostMapping("addVedio")
    public R addVedio(@RequestBody Vedio vedio){
        vedioService.addVedio(vedio);
        return R.ok();
    }

    @ApiOperation(value = "删除vedio")
    @DeleteMapping("deleteVedio/{id}")
    public R deleteVedio(@PathVariable String id){
        vedioService.deleteVedio(id);
        return R.ok();
    }


    @ApiOperation(value = "更新vedio")
    @PostMapping("updateVedio")
    public R updateVedio(@RequestBody Vedio vedio){
        vedioService.updateVedio(vedio);
        return R.ok();
    }


    @ApiOperation(value = "Web: 获取播放量前八的视频")
    @GetMapping("vedioListOrderByPlayCount")
    public R vedioListOrderByPlayCount(){
        return  vedioService.vedioListOrderByPlayCount();
    }

    @ApiOperation(value = "Web: 获取视频播放凭证")
    @GetMapping("getVedioPlayAuth/{id}")
    public R getVedioPlayAuth(@PathVariable String id) throws ClientException {
        return vedioService.editClickVedio(id);
    }


    @ApiOperation(value = "Web: 根据id得到vedioList")
    @PostMapping("getVedioListById/{id}/{page}/{limit}")
    public R getVedioListById(@PathVariable String id,
                              @PathVariable Long page,
                              @PathVariable Long limit,
                              @RequestBody(required = false) QueryVedioVo queryVedioVo) {
        return vedioService.getVedioListById(id,page,limit,queryVedioVo);
    }

    @ApiOperation(value = "Web: 根据id得到vedioList")
    @PostMapping("getVedioListByVedioId/{id}/{page}/{limit}")
    public R getVedioListByVedioId(@PathVariable String id,
                              @PathVariable Long page,
                              @PathVariable Long limit,
                              @RequestBody(required = false) QueryVedioVo queryVedioVo) {
        return vedioService.getVedioListByVedioId(id,page,limit,queryVedioVo);
    }



    @ApiOperation(value = "Web: 根据member到vedioList")
    @PostMapping("getMemberVedioList")
    public List<Vedio> getMemberVedioList(@RequestBody QueryVedio queryVedio) {
        List<Vedio> vedioList = vedioService.getMemberVedioList(queryVedio);
        return vedioList;
    }

}

