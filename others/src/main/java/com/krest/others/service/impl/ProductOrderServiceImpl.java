package com.krest.others.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krest.others.client.MemberClient;
import com.krest.others.client.ProductClient;
import com.krest.others.entity.*;
import com.krest.others.entity.Address;
import com.krest.others.entity.OrderItemVo;
import com.krest.others.entity.OrderVo;
import com.krest.others.entity.ProductOrder;
import com.krest.others.entity.ProductOrderDetail;
import com.krest.others.entity.SubmitOrderVo;
import com.krest.others.entity.vo.QueryOrder;
import com.krest.others.mapper.ProductOrderMapper;
import com.krest.others.service.CartService;
import com.krest.others.service.ProductOrderDetailService;
import com.krest.others.service.ProductOrderPaylogService;
import com.krest.others.service.ProductOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.utils.entity.*;
import com.krest.utils.myexception.myException;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-16
 */
@RabbitListener(queues = "order.release.queue")
@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrder> implements ProductOrderService {

    public static IdWorker idWorker = new IdWorker();

    @Autowired
    private MemberClient memberClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private CartService cartService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ProductOrderDetailService productOrderDetailService;

    @Autowired
    private ProductOrderPaylogService paylogService;


    /**
     * 监听超时订单，并自动取消
     */
    @RabbitHandler
    public void handleOrderRelease(SubmitOrderVo submitOrderVo, Message message, Channel channel) throws IOException {
        System.out.println("收到订单超时的消息");
        ProductOrder order = baseMapper.selectById(submitOrderVo.getOrder().getId());

        try{
            //更新订单信息
            if (order.getStatus()==1){
                System.out.println("订单已经支付，无需关闭操作");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }else{
                order.setStatus(3);
                System.out.println("更新订单信息:"+order);
                baseMapper.updateById(order);
                //更新库存工作单信息
                Integer integer = productClient.feignCloseStockWorklist(order.getId());
                if (integer==1){
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                }
                System.out.println("关闭延时订单成功");

                submitOrderVo.setOrder(order);
                MQOrder mqOrder = new MQOrder();
                BeanUtils.copyProperties(order,mqOrder);
                // 发送库存释放信息,让库存释放系统知道订单已经超时或取消，可以释放库存
                System.out.println("关闭订单的信息是："+order);
                try{
                    //todo 保证消息能够百分百的发送出去，发送消息之前，将消息保存到数据库当中
                    rabbitTemplate.convertAndSend("order-event-exchange","order.release.stock",mqOrder);
                    System.out.println("发送解锁库存成功");
                }catch (Exception e){
                    //todo 将没有发送的消息进行重试发送，定期扫描数据库，将没有处理的消息，重新发送的MQ中
                }
            }
        }catch (Exception e){
            System.out.println("关闭订单失败，消息重新回到队列中");
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }

    /**
     * 创建订单
     * @param memberId
     * @return
     */
    @Override
    public ProductOrder createOrder(String memberId) {
        // 检查用户是否登陆
        ProductOrder order = new ProductOrder();
        if(!StringUtils.isEmpty(memberId)){
            // 得到会员的详细信息
            Member memberInfo = memberClient.getOrderMemberInfo(memberId);
            order.setMemberId(memberInfo.getId());
            order.setNickname(memberInfo.getNickname());
            order.setMobile(memberInfo.getMobile());
            List<Address> addresses = memberClient.feignGetAddressByMemberId(memberId);

            // 远程查询所有购物车中的购物项
            CartVo cartVo = cartService.getCartVo(memberId);
            List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();
            List<CartItemVo> cartItemList = cartItemVoList.stream().map(cartItem ->{
                //获取商品的最新价格
                cartItem.setPrice(productClient.getPriceById(cartItem.getProductId()));
                return cartItem;
            } ).collect(Collectors.toList());
            cartVo.setCartItemVoList(cartItemList);

            // 获取库存信息
            List<OrderItemVo> orderItemVos = new ArrayList<>();
            for (CartItemVo cartItemVo : cartItemList) {
                OrderItemVo orderItemVo= new OrderItemVo();
                String productId = cartItemVo.getProductId();
                Stock stock = productClient.feignGetStockById(productId);
                BeanUtils.copyProperties(cartItemVo,orderItemVo);
                orderItemVo.setStockNum(stock.getStock());
                if (orderItemVo.getCountNum()>orderItemVo.getStockNum()){
                    order.setOutOfStock(true);
                }
                orderItemVos.add(orderItemVo);
            }
            OrderVo orderVo = new OrderVo();
            orderVo.setorderItemVoList(orderItemVos);

            //得到商品的信息
            order.setId(idWorker.nextId());
            order.setOrderVo(orderVo);
            order.setTotalNum(orderVo.getCountNum());
            order.setTotalFee(orderVo.getTotalAmount());
            order.setAddressList(addresses);

            if(!order.getOutOfStock()){
                //先删除固定前缀的key
                Set<String> keys=redisTemplate.keys(memberId+"*");
                redisTemplate.delete(keys);
                // 在Redis中放入一个防重复的令牌，避免多次提交
                String token = UUID.randomUUID().toString().replace("-", "");
                order.setOrderToken(token);
                redisTemplate.opsForValue().set(memberId+order.getId(),token);
            }
        }else{
            throw new myException(20001,"会员信息错误");
        }
        return order;
    }

    @Override
    public SubmitOrderVo submitOrder(OrderSubmitVo orderSubmitVo) {
        System.out.println("=============orderSubmitVo："+orderSubmitVo);
        SubmitOrderVo submitOrderVo = new SubmitOrderVo();
        // 下单流程：创建订单、验证令牌、验证价格、锁定库存
        String orderToken = orderSubmitVo.getOrderToken();
        String key=orderSubmitVo.getMemberId() + orderSubmitVo.getId();
        String script="if redis.call('get', KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        //令牌验证要通过,令牌的验证和删除必须保证原子性：使用脚本进行删除，防止用户多次点击，重复提交，0 校验失败
        Long result =(Long) redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList(key), orderToken);

        if (result==0L){
            //删除令牌失败
            System.out.println("删除令牌失败");
            return submitOrderVo;
        }else{
            System.out.println("令牌验证成功");
            //令牌验证成功
            // 下单流程：创建订单需要的信息、锁定库存
            ProductOrder productOrder = new ProductOrder();
            productOrder.setId(orderSubmitVo.getId());
            productOrder.setMemberId(orderSubmitVo.getMemberId());
            productOrder.setNickname(orderSubmitVo.getNickname());
            productOrder.setMobile(orderSubmitVo.getMobile());
            productOrder.setAddress(orderSubmitVo.getAddress());
            productOrder.setTotalNum(orderSubmitVo.getTotalNum());
            productOrder.setOrderVo(orderSubmitVo.getOrderVo());
            productOrder.setTotalFee(new BigDecimal(0.01));


            //开始锁定库存 利用RabbitMQ进行锁定库存，实现分布式事物
            Integer integer = productClient.feignLockStockByLists(orderSubmitVo);
            if (integer==0){
                // 锁定库存失败
                productClient.feignCloseStockWorklist(orderSubmitVo.getId());
                submitOrderVo.setCode(0);
                return submitOrderVo;
            }else{
                // 生成订单
                baseMapper.insert(productOrder);
                // 生成订单详情
                List<OrderItemVo> orderItemVos = orderSubmitVo.getOrderVo().getorderItemVoList();
                for (OrderItemVo orderItemVo : orderItemVos) {
                    ProductOrderDetail productOrderDetail=new ProductOrderDetail();
                    productOrderDetail.setId(idWorker.nextId());
                    productOrderDetail.setOrderId(orderSubmitVo.getId());
                    productOrderDetail.setProductId(orderItemVo.getProductId());
                    productOrderDetail.setNum(orderItemVo.getCountNum());
                    productOrderDetail.setPayPrice(orderItemVo.getPrice());
                    productOrderDetailService.save(productOrderDetail);
                }

                // 将提交的信息分给submitOrderVo
                submitOrderVo.setCode(1);
                submitOrderVo.setOrder(productOrder);
                rabbitTemplate.convertAndSend("order-event-exchange","order.create.order",submitOrderVo);
                return submitOrderVo;
            }
        }

    }

    @Override
    public List<ProductOrder> getOrderList(String memberId) {
        QueryWrapper<ProductOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id",memberId);
        queryWrapper.orderByDesc("gmt_create");
        List<ProductOrder> productOrders = baseMapper.selectList(queryWrapper);

        List<ProductOrder> collect = productOrders.stream().map(productOrder->{
            switch (productOrder.getStatus()){
                case 0:
                    productOrder.setStatusDescrpt("待支付");
                    break;
                case 1:
                    productOrder.setStatusDescrpt("已支付");
                    break;
                case 2:
                    productOrder.setStatusDescrpt("已取消");
                    break;
                case 3:
                    productOrder.setStatusDescrpt("超时取消");
                    break;
                default:
                    productOrder.setStatusDescrpt("订单状态未知");
                    break;
            }
                // 获取订单详情
            String orderId = productOrder.getId();
            QueryWrapper<ProductOrderDetail> productOrderDetailQueryWrapper=new QueryWrapper<>();
            productOrderDetailQueryWrapper.eq("order_id",orderId);
            List<ProductOrderDetail> productOrderDetails = productOrderDetailService.list(productOrderDetailQueryWrapper);

            // 获取订单内的商品详情
            List<ProductOrderDetail> collect1 = productOrderDetails.stream().map(productOrderDetail -> {

                return productOrderDetail.setProduct(productClient.getOrderProductById(productOrderDetail.getProductId()));
            }).collect(Collectors.toList());

            productOrder.setDetails(collect1);
            productOrder.setTotalNum(productOrderDetails.size());
            return productOrder;
        }).collect(Collectors.toList());

        List<ProductOrder> result = new ArrayList<>();
        for (ProductOrder productOrder : collect) {
            int num=0;
            List<ProductOrderDetail> details = productOrder.getDetails();
            for (ProductOrderDetail detail : details) {
                num=num+detail.getNum();
            }
            productOrder.setTotalNum(num);
            result.add(productOrder);
        }

        return result;
    }

    @Override
    public R listOrderList(Long page, Long limit, QueryOrder queryOrder) {
        QueryWrapper<ProductOrder> queryWrapper = new QueryWrapper<>();
        Page<ProductOrder> productOrderPage = new Page<>(page,limit);

        if(!StringUtils.isEmpty(queryOrder.getMemberId())){
            queryWrapper.eq("member_id",queryOrder.getMemberId());
        }

        if(!StringUtils.isEmpty(queryOrder.getMemberName())){
            queryWrapper.like("nickname",queryOrder.getMemberName());
        }
        if(queryOrder.getStatus() != null){
            queryWrapper.eq("status",queryOrder.getStatus());
        }

        IPage<ProductOrder> productOrderIPage = baseMapper.selectPage(productOrderPage, queryWrapper);
        long total = productOrderIPage.getTotal();
        List<ProductOrder> records = productOrderIPage.getRecords();

        List<ProductOrder> collect = records.stream().map(record -> {
            return editOrderInfo(record);
        }).collect(Collectors.toList());

        return R.ok().data("total",total).data("record",collect );
    }

    @Override
    public R deleteOrderById(String id) {
        // 删除订单详情、删除订单信息、删除订单支付信息
        QueryWrapper<ProductOrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id",id);
        productOrderDetailService.remove(queryWrapper);

        ProductOrder productOrder = baseMapper.selectById(id);
        if(productOrder.getStatus()==1){
            QueryWrapper<ProductOrderPaylog> paylogQueryWrapper = new QueryWrapper<>();
            paylogQueryWrapper.eq("order_no",id);
            paylogService.remove(paylogQueryWrapper);
        }

        baseMapper.deleteById(id);

        return R.ok();
    }

    private ProductOrder editOrderInfo(ProductOrder record) {
        if (record.getStatus()==0){
            record.setStatusDescrpt("待支付");
        }else if(record.getStatus()==1){
            record.setStatusDescrpt("成功支付");
        }else if(record.getStatus()==3){
            record.setStatusDescrpt("订单超时");
        }

        String orderId = record.getId();
        QueryWrapper<ProductOrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id",orderId);
        int count = productOrderDetailService.count(queryWrapper);
        record.setTotalNum(count);
        return record;
    }
}
