package com.krest.utils.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author krest
 * @since 2020-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProductOrder对象", description="订单表")
public class ProductOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单号")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "会员id")
    private String memberId;

    @ApiModelProperty(value = "会员昵称")
    private String nickname;

    @ApiModelProperty(value = "会员手机")
    private String mobile;

    @TableField(exist = false)
    @ApiModelProperty(value = "地址列表")
    private List<Address> addressList;

    @ApiModelProperty(value = "收货地址")
    private String address;

    @TableField(exist = false)
    @ApiModelProperty(value = "地址列表")
    private OrderVo orderVo;

    @TableField(exist = false)
    @ApiModelProperty(value = "地址列表")
    private Boolean outOfStock=false;

    @TableField(exist = false)
    @ApiModelProperty(value = "放重复提交令牌")
    private String orderToken;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品数量")
    private Integer totalNum;

    @ApiModelProperty(value = "订单金额（分）")
    private BigDecimal totalFee;

    @TableField(exist = false)
    private List<ProductOrderDetail> details;

    @ApiModelProperty(value = "支付类型（1：微信 2：支付宝）")
    private Integer payType;

    @ApiModelProperty(value = "订单状态（0：未支付 1：已支付 2：待定 3：订单取消）")
    private Integer status;

    @TableField(exist = false)
    private String statusDescrpt;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
