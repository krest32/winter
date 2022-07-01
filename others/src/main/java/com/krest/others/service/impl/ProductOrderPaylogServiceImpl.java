package com.krest.others.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.krest.others.config.HttpClient;
import com.krest.others.entity.ProductOrder;
import com.krest.others.entity.ProductOrderPaylog;
import com.krest.others.mapper.ProductOrderPaylogMapper;
import com.krest.others.service.ProductOrderPaylogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.others.service.ProductOrderService;

import com.krest.utils.utils.IdWorker;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-24
 */
@Service
public class ProductOrderPaylogServiceImpl extends ServiceImpl<ProductOrderPaylogMapper, ProductOrderPaylog> implements ProductOrderPaylogService {

    @Autowired
    private ProductOrderService productOrderService;

    public static IdWorker idWorker = new IdWorker();

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public Map createNative(String orderNo) {

        try {
            //根据订单id获取订单信息
            ProductOrder order = productOrderService.getById(orderNo);
            Map m = new HashMap();
            //1、设置支付参数
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getMemberId());
            m.put("out_trade_no", orderNo);
            // 将价格变成字符串
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");
            m.put("spbill_create_ip", "127.0.0.1");
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            m.put("trade_type", "NATIVE");

            //2、HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //client设置参数 将Map转化位Xml工具，并且加密
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            //将Map转化位Xml工具
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //4、封装返回结果集

            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("memberId", order.getMemberId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));
            map.put("code_url", resultMap.get("code_url"));

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);
            return map;

        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();

            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //6、转成Map
            //7、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //获取订单id
        String orderNo = map.get("out_trade_no");
        //根据订单id查询订单信息
        ProductOrder order = productOrderService.getById(orderNo);
        Integer status = order.getStatus();
        System.out.println("status:+==========================="+status);

        if (status == null) {
            order.setStatus(1);
            productOrderService.updateById(order);
        }

        QueryWrapper<ProductOrderPaylog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);
        Integer count = baseMapper.selectCount(queryWrapper);

        if(count == 0){
            //记录支付日志
            ProductOrderPaylog payLog=new ProductOrderPaylog();
            payLog.setId(idWorker.nextId());

            //支付订单号
            payLog.setOrderNo(order.getId());

            payLog.setPayTime(new Date());
            //支付类型
            payLog.setPayType(1);
            //总金额(分)
            payLog.setTotalFee(order.getTotalFee());
            //支付状态
            payLog.setTradeState(map.get("trade_state"));
            payLog.setTransactionId(map.get("transaction_id"));

            payLog.setAttr(JSONObject.toJSONString(map));
            System.out.println("payLog:"+payLog);
            baseMapper.insert(payLog);
        }

    }
}