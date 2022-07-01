package com.krest.others.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/11/20 12:28
 * @Description:
 */
public class CartVo {
    private List<CartItemVo> cartItemVoList;

    private Integer countNum;

    public List<CartItemVo> getCartItemVoList() {
        return cartItemVoList;
    }

    public void setCartItemVoList(List<CartItemVo> cartItemVoList) {
        this.cartItemVoList = cartItemVoList;
    }

    public Integer getCountNum() {
        int countNum = 0;
        if(cartItemVoList!=null&&cartItemVoList.size()>0){
            for (CartItemVo cartItem : cartItemVoList) {
                countNum +=cartItem.getCountNum();
            }
        }
        return countNum;
    }


    public Integer getCountType() {
        int countType = 0;
        if(cartItemVoList!=null&&cartItemVoList.size()>0){
            for (CartItemVo cartItem : cartItemVoList) {
                countType += 1;
            }

        }
        return countType;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal totalAmount = new BigDecimal(0.00);
        if(cartItemVoList!=null&&cartItemVoList.size()>0){
            for (CartItemVo cartItem : cartItemVoList) {
                BigDecimal totalPrice = cartItem.getTotalPrice();
                totalAmount=totalAmount.add(totalPrice);
            }
        }
        return totalAmount.subtract(this.getReduce());
    }

    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }

    private Integer countType;

    private BigDecimal totalAmount;

    private BigDecimal reduce=new BigDecimal(0.00);
}
