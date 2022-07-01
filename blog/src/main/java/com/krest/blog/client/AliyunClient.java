package com.krest.blog.client;

import com.krest.utils.response.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
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

}
