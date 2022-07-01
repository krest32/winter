package com.krest.vedio.client;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author: krest
 * @date: 2021/5/10 18:04
 * @description:
 */
@Component
@FeignClient("member")
public interface VedioCollect {

    @ApiOperation("根据视频Id获取视频收藏数量")
    @GetMapping("/member/vediocollect/countCollection/{id}")
    public int countCollection(@PathVariable("id") String id);
}
