package com.krest.others.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.utils.StringUtils;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.krest.others.Utlis.AliyunVodSDKUtils;
import com.krest.others.Utlis.ConstantPropertiesUtils;
import com.krest.others.Utlis.ConstantVodUtils;
import com.krest.others.service.OthersService;
import com.krest.utils.myexception.myException;
import com.krest.utils.response.R;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

/**
 * @Auther: krest
 * @Date: 2020/12/4 17:04
 * @Description:
 */
@Service
public class OthersServiceImpl implements OthersService {
    @Override
    public boolean send(String phone, String sms_180051135, Map<String, Object> param) {
        if(StringUtils.isEmpty(phone)) {
            return false;
        }
        //阿里云平台的验证信息
        DefaultProfile profile =
                DefaultProfile.getProfile("default", "LTAI4GGrSpTcJz3m5j6aGmeN", "fl2rnbHvYcijqLngBzUZAKTxB8iihW");
        IAcsClient client = new DefaultAcsClient(profile);

        //固定的请求参数的设置
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //短信请求模板
        request.putQueryParameter("PhoneNumbers", phone);   //发送短信的手机号码
        request.putQueryParameter("SignName", "谷维在线教育网站");  //短信模板的名字
        request.putQueryParameter("TemplateCode","SMS_192380858");  //短信模板的ID
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));   //个数转换位Json

        //打印信息
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public R upload(MultipartFile file) {

        //基本的信息配置
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;
        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 上传文件流。
            InputStream inputStream = file.getInputStream();

            //获取文件
            String fileName = file.getOriginalFilename();
            //生成随机的数字,让文件名称不会重复，然后去掉id中的中划线
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");

            fileName = uuid + fileName;

//            //虽然分组方便了管理，但是给删除文件带来了巨大的空难
//            //获取当前的日期，然后给文件进行分组准备
//            String datePath = new DateTime().toString("yyyy/MM/dd");
//
//            //拼接
//            fileName = datePath + "/" + fileName;

            //第一个参数 Bucket名称
            //第二个参数 上传到oss文件路径和名称
            //第三个参数 上传文件的输入流
            ossClient.putObject(bucketName, fileName, inputStream);


            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传Aliyun的文件拼接出来
            //https://duxin1010.oss-cn-beijing.aliyuncs.com/IMG_20190127_234844.jpg

            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            return R.ok().data("url",url).data("filename",fileName);

        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void deleteOss(String ossAdress) {

        //基本的信息配置
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;
        String objectName=ossAdress;
        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            System.out.println("删除图片:"+objectName);
            // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
            ossClient.deleteObject(bucketName, objectName);
            // 关闭OSSClient。
            ossClient.shutdown();


        } catch (Exception e) {
            throw  new myException(20000,"删除错误");
        }
    }

    @Override
    public String uploadVedioAliyun(MultipartFile file) {

        String fileName=file.getOriginalFilename();
        //去掉上传文件的后缀名称
        String title=fileName.substring(0,fileName.lastIndexOf("."));
        InputStream inputStream= null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //上传视频请求：需要有阿里云KeyId，ACCESSID，文件标题
        UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.KEY_ID, ConstantVodUtils.KEY_SECRET, title, fileName, inputStream);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        String videoId = response.getVideoId();

        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
        return videoId;
    }

    @Override
    public void removeVideo(String videoId) {
        try{
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantVodUtils.KEY_ID,
                    ConstantVodUtils.KEY_SECRET);

            DeleteVideoRequest request = new DeleteVideoRequest();

            request.setVideoIds(videoId);

            DeleteVideoResponse response = client.getAcsResponse(request);

            System.out.print("RequestId = " + response.getRequestId() + "\n");

        }catch (ClientException e){
            throw new myException(20001, "视频删除失败");
        }
    }
}
