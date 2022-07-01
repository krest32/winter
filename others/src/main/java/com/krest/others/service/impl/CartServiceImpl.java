package com.krest.others.service.impl;

import com.alibaba.fastjson.JSON;

import com.krest.others.client.MemberClient;
import com.krest.others.client.ProductClient;
import com.krest.others.entity.CartItemVo;
import com.krest.others.entity.CartVo;
import com.krest.others.entity.UserInfoVO;
import com.krest.others.service.CartService;
import com.krest.utils.entity.Product;
import com.krest.utils.myexception.myException;
import com.krest.utils.response.R;
import com.krest.utils.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: krest
 * @Date: 2020/11/20 12:19
 * @Description:
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {

    public static ThreadLocal<UserInfoVO> threadLocal = new ThreadLocal<>();

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MemberClient memberClient;

    @Autowired
    private ProductClient productClient;

    private final String CART_PREFIE = "winter:cart:";



    //得到购物车中的所有信息
    @Override
    public R cartListPage(HttpServletRequest request) {
        CartVo cartVo = new CartVo();
        CheckUser(request);
        UserInfoVO userInfoVO = threadLocal.get();

        System.out.println("得到用户信息："+userInfoVO);
        if (userInfoVO.getMemberId()!=null&&userInfoVO.getMemberId().length()>10){
            //获取登陆情况下的购物车
            String memerbId = CART_PREFIE + userInfoVO.getMemberId();
            // 判断临时购物车是否有数据
            String userTempKey = CART_PREFIE + userInfoVO.getUserTempKey();

            System.out.println("得到临时购物车前缀："+userTempKey);

            List<CartItemVo> tempCartItem = getCartItems(userTempKey);
            //如果临时购物车不为空
            System.out.println("得到临时购物车列表："+tempCartItem);

            if (tempCartItem!=null && tempCartItem.size()>0){
                System.out.println("判断临时购物车");
                //说明临时购物车存在数据,进行合并购物车
                for (CartItemVo cartItemVo : tempCartItem) {
                    System.out.println("cartItemVo"+ cartItemVo.getProductId());
                    addCartItem(cartItemVo.getProductId(),cartItemVo.getCountNum(),request);
                }
                //添加完成之后删除临时购物车
                redisTemplate.delete(userTempKey);
                //然后获取购物车的数据
                //得到登陆的购物车
                List<CartItemVo> CartItems = getCartItems(memerbId);
                cartVo.setCartItemVoList(CartItems);
            }

            List<CartItemVo> cartItems = getCartItems(memerbId);
            System.out.println("得到用户购物车数据："+cartItems);
            cartVo.setCartItemVoList(cartItems);

        }else{
            String userTempKey = CART_PREFIE + userInfoVO.getUserTempKey();
            System.out.println("得到临时购物车前缀："+userTempKey);
            List<CartItemVo> tempCartItem = getCartItems(userTempKey);
            cartVo.setCartItemVoList(tempCartItem);
        }
        return R.ok().data("cartVo",cartVo);
    }


    //加入到购物车
    @Override
    public R addCartItem(String priductId, Integer num, HttpServletRequest request) {
        if(num == null){
            num=1;
        }

        BoundHashOperations operations = getCart(request);
        String checkIsExistProduct= (String) operations.get(priductId);

        //如果购物车无此商品
        if(StringUtils.isEmpty(checkIsExistProduct)){
            //远程查询要添加的商品的信息
            Product product = productClient.getOrderProductById(priductId);
            //添加新商品到购物车
            CartItemVo cartItemVo = new CartItemVo();
            cartItemVo.setProductId(priductId);
            cartItemVo.setCheck(true);
            cartItemVo.setCountNum(num);
            cartItemVo.setPicture(product.getPicture());
            cartItemVo.setPictureName(product.getPictureName());
            cartItemVo.setProductTitle(product.getTitle());
            cartItemVo.setPrice(product.getPrice());

            String s = JSON.toJSONString(cartItemVo);
            operations.put(priductId, s);

        }else {
            // 如果购物车由此商品,修改数量即可
            CartItemVo cartItemVo = JSON.parseObject(checkIsExistProduct, CartItemVo.class);
            cartItemVo.setCountNum(cartItemVo.getCountNum()+num);
            //然后再将数据放回到Redis购物车中
            operations.delete(priductId);
            String s=JSON.toJSONString(cartItemVo);
            operations.put(priductId,s);
        }
        return R.ok();
    }

    @Override
    public R changeCartItemNum(String productId, Integer num, HttpServletRequest request) {
        CheckUser(request);
        if(num == null){
            throw new myException(20001,"数量信息为空");
        }
        BoundHashOperations operations = getCart(request);
        // 检查是否存在该信息
        String checkIsExistProduct= (String) operations.get(productId);
        //如果购物车无此商品

        if(StringUtils.isEmpty(checkIsExistProduct)){
            throw new myException(20001,"购物车没有相应的购物信息");
        }else {
            // 如果购物车由此商品,修改数量即可
            CartItemVo cartItemVo = JSON.parseObject(checkIsExistProduct, CartItemVo.class);
            cartItemVo.setCountNum(num);
            // 然后再将数据放回到Redis购物车中
            operations.delete(productId);
            // 将数据重新保存到redis中
            String s=JSON.toJSONString(cartItemVo);
            operations.put(productId,s);
        }
        return R.ok();
    }

    @Override
    public R deleteCartItem(String productId,HttpServletRequest request) {
        if(productId == null && StringUtils.isEmpty(productId)){
            throw new myException(20001,"商品Id为空");
        }
        BoundHashOperations operations = getCart(request);
        // 检查是否存在该信息
        String checkIsExistProduct= (String) operations.get(productId);

        //如果购物车无此商品
        if(StringUtils.isEmpty(checkIsExistProduct)){
            System.out.println("删除购物车出错");
            throw new myException(20001,"购物车没有相应的购物信息");
        }else {
            // 然后再将数据放回到Redis购物车中
            operations.delete(productId);
            System.out.println("删除购物车数据成功");
            return R.ok();
        }
    }



    @Override
    public CartVo getCartVo(String memberId) {
        CartVo cartVo = new CartVo();
        String memerbId = CART_PREFIE + memberId;

        List<CartItemVo> CartItems = getCartItems(memerbId);
        cartVo.setCartItemVoList(CartItems);

        List<CartItemVo> cartItems = getCartItems(memerbId);
        cartVo.setCartItemVoList(cartItems);
        return cartVo;
    }


    private List<CartItemVo> getCartItems(String cartKey){
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(cartKey);
        List<String> values = boundHashOperations.values();

        if (values!=null&&values.size()>0){
            List<CartItemVo> list = values.stream().map(obj->{
                String str = (String)obj;
                System.out.println("String CartItem"+str);
                CartItemVo cartItemVo = JSON.parseObject(str,CartItemVo.class);
                System.out.println("cartItemVo"+cartItemVo);
                return cartItemVo;
            }).collect(Collectors.toList());
            return list;
        }
        return null;

    }

    //获取当前用户的购物车
    private BoundHashOperations getCart(HttpServletRequest request) {
        // 获取当前的用户
        // UserInfoVO userInfoVO = CartIntercepter.threadLocal.get();
        UserInfoVO userInfoVO= CheckUser(request);
        String cartKey="";
        // 决定使用哪一个购物车
        if (userInfoVO.getMemberId()!=null){
            cartKey=CART_PREFIE+userInfoVO.getMemberId();
        }else{
            cartKey=CART_PREFIE+userInfoVO.getUserTempKey();
        }
        //获取Redis中的购物测信息
        BoundHashOperations boundHashOps = redisTemplate.boundHashOps(cartKey);
        return boundHashOps;
    }

    private UserInfoVO CheckUser(HttpServletRequest request){

        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        UserInfoVO userInfoVO = new UserInfoVO();
        // memberId为空，用户没有登陆,
        if (!memberId.isEmpty() && memberId!=null ){
            userInfoVO.setMemberId(memberId);
            userInfoVO.setTempUSer(false);
        }
        //检查是否存在user-key
        String userKey = request.getHeader("user-key");
        if (!StringUtils.isEmpty(userKey)){
            userInfoVO.setUserTempKey(userKey);
        }
        // 检查客户端是否禁用cooikes
        if( memberId.isEmpty() && StringUtils.isEmpty(userKey) && memberId==null && userKey==null ){
            throw new myException(400,"没有用户信息(包括没有临时用户信息)");
        }
        //将信息保存在 threadLocal 中
        threadLocal.set(userInfoVO);

        return userInfoVO;
    }
}
