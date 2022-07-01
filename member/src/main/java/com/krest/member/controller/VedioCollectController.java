package com.krest.member.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krest.member.entity.VedioCollect;
import com.krest.member.entity.Vo.QueryVedio;
import com.krest.member.service.VedioCollectService;
import com.krest.utils.entity.ResponseVedio;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 会员视频收藏表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Api(value = "会员中心",tags ="会员中心")
@CrossOrigin
@RestController
@RequestMapping("/member/vediocollect")
public class VedioCollectController {

    @Autowired
    private VedioCollectService vedioCollectService;

    @ApiOperation(value = "添加收藏视频")
    @PostMapping("addCollectVedio")
    public R addCollectVedio(@RequestBody VedioCollect vedioCollect){
        vedioCollectService.addCollectVedio(vedioCollect);
        return R.ok();

    }

    @ApiOperation("取消收藏视频")
    @PostMapping("cancelCollect")
    public R cancelCollect(@RequestBody VedioCollect vedioCollect){
        QueryWrapper<VedioCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id",vedioCollect.getMemberId());
        queryWrapper.eq("vedio_id",vedioCollect.getVedioId());
        vedioCollectService.remove(queryWrapper);
        return R.ok();
    }

    @ApiOperation("根据视频Id获取视频收藏数量")
    @GetMapping("countCollection/{id}")
    public Integer countCollection(@PathVariable String id){
        QueryWrapper<VedioCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vedio_id",id);
        System.out.println(id);
        int count = vedioCollectService.count(queryWrapper);
        return count;
    }

}

