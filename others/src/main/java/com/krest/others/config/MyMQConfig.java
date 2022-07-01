package com.krest.others.config;


import com.krest.others.entity.ProductOrder;
import com.krest.utils.entity.Product;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: krest
 * @Date: 2020/12/24 16:36
 * @Description:
 */
@Configuration
public class MyMQConfig {

    // 消息序列化，使用json格式
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

//    @RabbitListener(queues = "order.release.queue")
//    public void listener(ProductOrder order, Channel channel, Message message) throws IOException {
//        System.out.println("收到过期的订单消息:"+order.getId());
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
//    }

//    @RabbitListener(queues = "order.delay.queue")
//    public void createListener(ProductOrder order){
//        System.out.println("收到新的的订单消息:"+order.getId());
//    }

    @Bean
    public Queue orderDelayQueue(){
        // 延时队列
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","order-event-exchange");
        arguments.put("x-dead-letter-routing-key","order.release.order");
        arguments.put("x-message-ttl",60000);
        Queue orderDelayQueue = new Queue("order.delay.queue", true, false, false,arguments);
        return orderDelayQueue;
    }

    @Bean
    public Queue orderReleaseQueue(){
        // 普通队列
        Queue orderDelayQueue = new Queue("order.release.queue", true, false, false);
        return orderDelayQueue;
    }

    @Bean
    public Exchange orderEventExchange(){
        Exchange exchange = new TopicExchange("order-event-exchange",true,false);
        return exchange;
    }

    @Bean
    public Binding orderCreateBinging(){
        Binding binding = new Binding("order.delay.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.create.order",
                null);
        return binding;
    }


    /**
     *  库存释放
     */
    @Bean
    public Binding stockReleaseBinging(){
        Binding binding = new Binding("stock.release.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.stock",
                null);
        return binding;
    }


    @Bean
    public Binding orderReleaseBingding(){
        Binding binding = new Binding("order.release.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.order",
                null);
        return binding;
    }


}
