package com.krest.vedio.client;

import com.krest.utils.response.R;
import com.netflix.client.ClientException;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Auther: krest
 * @Date: 2020/12/4 23:31
 * @Description:
 */
@Component
@FeignClient("others")
public interface AliyunClient {

    @ApiOperation(value = "删除图片")
    @DeleteMapping("/others/Aliyun/deleteOss/{ossAdress}")
    public R deleteOss(@PathVariable("ossAdress") String ossAdress);

    @ApiOperation(value = "删除视频方法" )
    @DeleteMapping("/others/Aliyun/removeVedioById/{videoId}")
    public R removeVideo(@PathVariable("videoId") String videoId);


    @ApiOperation(value = "视频点播功能")
    @GetMapping("/others/Aliyun/getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id) throws ClientException;
}
