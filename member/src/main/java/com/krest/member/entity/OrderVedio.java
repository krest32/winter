package com.krest.member.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

import com.krest.utils.entity.Album;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 视频购买订单
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("member_order_vedio")
@ApiModel(value="OrderVedio对象", description="视频购买订单")
public class OrderVedio implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单号")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "专辑id")
    private String albumId;

    @TableField(exist = false)
    @ApiModelProperty(value = "专辑信息")
    private Album albumInfo;

    @ApiModelProperty(value = "会员id")
    private String memberId;

    @TableField(exist = false)
    @ApiModelProperty(value = "会员姓名")
    private String memberName;

    @ApiModelProperty(value = "订单金额（分）")
    private BigDecimal totalFee;

    @ApiModelProperty(value = "支付类型（1：微信 2：支付宝）")
    private Integer payType;

    @ApiModelProperty(value = "订单状态（0：未支付 1：已支付）")
    private Integer status;

    @TableField(exist = false)
    @ApiModelProperty(value = "支付类型（1：微信 2：支付宝）")
    private String payTypeString;

    @TableField(exist = false)
    @ApiModelProperty(value = "订单状态（0：未支付 1：已支付）")
    private String payStatus;

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
