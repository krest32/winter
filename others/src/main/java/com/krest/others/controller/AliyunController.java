package com.krest.others.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.utils.StringUtils;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.krest.others.Utlis.ConstantVodUtils;
import com.krest.others.Utlis.RandomUtil;
import com.krest.others.Utlis.initVodClient;
import com.krest.others.service.OthersService;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: krest
 * @Date: 2020/12/4 17:05
 * @Description:
 */
@Api(value = "阿里云服务",tags ="阿里云服务")
@CrossOrigin
@RestController
@RequestMapping("/others/Aliyun")
public class AliyunController {

    @Autowired
    private OthersService othersService;

    @Autowired  //redis请求服务
    private RedisTemplate<String, String> redisTemplate;

    @ApiOperation(value = "发送手机验证码" )
    @GetMapping(value = "/send/{phone}")
    public R code(@PathVariable String phone) {
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)) {
            return R.ok();
        }
        code = RandomUtil.getFourBitRandom();
        Map<String,Object> param = new HashMap<>();
        param.put("code", code);
        System.out.println(code);
        boolean isSend = othersService.send(phone, "SMS_180051135", param);
        if(isSend) {
            redisTemplate.opsForValue().set(phone, code,5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("发送短信失败");
        }
    }

    @ApiOperation(value = "图片上传")
    @PostMapping("oss")
    public R uploadOssFile(MultipartFile file){
        return othersService.upload(file);
    }


    @ApiOperation(value = "删除图片")
    @DeleteMapping("deleteOss/{ossAdress}")
    public R deleteOss(@PathVariable String ossAdress){
        othersService.deleteOss(ossAdress);
        return R.ok();
    }

    @ApiOperation(value = "上传视频方法" )
    @PostMapping("uploadvideo")
    public R uploadVideo(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file) throws Exception {
        String videoId = othersService.uploadVedioAliyun(file);
        return R.ok().message("视频上传成功").data("videoId", videoId);
    }

    @ApiOperation(value = "删除视频方法" )
    @DeleteMapping("removeVedioById/{videoId}")
    public R removeVideo(@PathVariable String videoId) {
        othersService.removeVideo(videoId);
        return R.ok().message("视频删除成功");
    }


    @ApiOperation(value = "视频点播功能")
    @GetMapping("getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id) throws ClientException {

        DefaultAcsClient client = initVodClient.initVodClient(ConstantVodUtils.KEY_ID, ConstantVodUtils.KEY_SECRET);
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(id);
        GetVideoPlayAuthResponse response = client.getAcsResponse(request);
        String playAuth = response.getPlayAuth();
        return R.ok().data("playAuth", playAuth);
    }
}
