package com.krest.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.krest.product.Product8005;
import com.krest.product.client.AliyunClient;
import com.krest.utils.entity.Product;
import com.krest.utils.entity.ProductOrder;
import com.krest.utils.response.R;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: krest
 * @date: 2021/6/16 15:52
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Product8005.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class test {

    @Autowired
    private AliyunClient aliyunClient;

    @Test
    public void test(){
        R qrderById = aliyunClient.QueryOrderById("1342718451281657856");
        Object object = qrderById.getData().get("order");
        ProductOrder order = JSON.parseObject(JSON.toJSONString(object), new TypeReference<ProductOrder>() { });
        System.out.println(order);
    }
}
