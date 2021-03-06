package com.krest.others.Utlis;


import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:application.yml")
public class ConstantVodUtils implements InitializingBean {

    @Value("${aliyun.vod.file.keyid}")
    private  String keyid;
    @Value("${aliyun.vod.file.keysecret}")
    private  String keysecret;

    public static String KEY_ID;
    public static String KEY_SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        KEY_ID=keyid;
        KEY_SECRET=keysecret;
    }
}