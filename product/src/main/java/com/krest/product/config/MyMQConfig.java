package com.krest.product.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: krest
 * @Date: 2020/12/24 16:36
 * @Description:
 */
@Configuration
public class MyMQConfig {

//    @RabbitListener(queues = "order.release.queue")
//    public void listener(ProductOrder order, Channel channel, Message message) throws IOException {
//        System.out.println("收到过期的订单消息:"+order.getId());
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
//    }

//    @RabbitListener(queues = "order.delay.queue")
//    public void createListener(ProductOrder order){
//        System.out.println("收到新的的订单消息:"+order.getId());
//    }

    // 消息序列化，使用json格式
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue stockDelayQueue(){
        // 延时队列
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","stock-event-exchange");
        arguments.put("x-dead-letter-routing-key","stock.release");
        arguments.put("x-message-ttl",100000);
        Queue stockDelayQueue = new Queue("stock.delay.queue", true, false, false,arguments);
        return stockDelayQueue;
    }

    @Bean
    public Queue stockReleaseQueue(){
        // 普通队列
        Queue stockReleaseQueue = new Queue("stock.release.queue", true, false, false);
        return stockReleaseQueue;
    }

    /**
     * 创建交换机
     */
    @Bean
    public Exchange stockEventExchange(){
        Exchange exchange = new TopicExchange("stock-event-exchange",true,false);
        return exchange;
    }

    @Bean
    public Binding stockLockedBinging(){
        Binding binding = new Binding("stock.delay.queue",
                Binding.DestinationType.QUEUE,
                "stock-event-exchange",
                "stock.locked",
                null);
        return binding;
    }

    @Bean
    public Binding stockReleaseBingding(){
        Binding binding = new Binding("stock.release.queue",
                Binding.DestinationType.QUEUE,
                "stock-event-exchange",
                "stock.release.#",
                null);
        return binding;
    }


}
