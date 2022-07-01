package com.krest.others.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/11/20 12:22
 * @Description:
 */
public class OrderItemVo {
    private String productId;
    private String productTitle;
    private Boolean check=true;
    private Integer countNum;
    private Long stockNum;
    private List<String> Attr;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private String picture;
    private String pictureName;

    public Long getStockNum() {
        return stockNum;
    }

    public void setStockNum(Long stockNum) {
        this.stockNum = stockNum;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public Integer getCountNum() {
        return countNum;
    }

    public void setCountNum(Integer countNum) {
        this.countNum = countNum;
    }

    public List<String> getAttr() {
        return Attr;
    }

    public void setAttr(List<String> attr) {
        Attr = attr;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = this.price.multiply(new BigDecimal("" + this.countNum));
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }
}
