package com.krest.product.entity.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/11/20 12:28
 * @Description:
 */
public class OrderVo {

    private List<OrderItemVo> orderItemVoList;

    private Integer countNum;

    public List<OrderItemVo> getorderItemVoList() {
        return orderItemVoList;
    }

    public void setorderItemVoList(List<OrderItemVo> orderItemVoList) {
        this.orderItemVoList = orderItemVoList;
    }

    public Integer getCountNum() {
        int countNum = 0;
        if(orderItemVoList!=null&&orderItemVoList.size()>0){
            for (OrderItemVo orderItemVo : orderItemVoList) {
                countNum +=orderItemVo.getCountNum();
            }
        }
        return countNum;
    }


    public Integer getCountType() {
        int countType = 0;
        if(orderItemVoList!=null&&orderItemVoList.size()>0){
            for (OrderItemVo orderItemVo : orderItemVoList) {
                countType += 1;
            }

        }
        return countType;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal totalAmount = new BigDecimal(0.00);
        if(orderItemVoList!=null&&orderItemVoList.size()>0){
            for (OrderItemVo orderItemVo : orderItemVoList) {
                BigDecimal totalPrice = orderItemVo.getTotalPrice();
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
