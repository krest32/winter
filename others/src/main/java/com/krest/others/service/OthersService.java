package com.krest.others.service;

import com.krest.utils.response.R;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Auther: krest
 * @Date: 2020/12/4 17:04
 * @Description:
 */

public interface OthersService {
    boolean send(String phone, String sms_180051135, Map<String, Object> param);

    R upload(MultipartFile file);

    void deleteOss(String ossAdress);

    String uploadVedioAliyun(MultipartFile file);

    void removeVideo(String videoId);
}
