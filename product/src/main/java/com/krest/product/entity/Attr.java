package com.krest.product.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品属性描述
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product_attr")
@ApiModel(value="Attr对象", description="商品属性描述")
public class Attr implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "商品ID")
    private String productId;

    @ApiModelProperty(value = "属性分组Id")
    private String attrGroupId;

    @TableField(exist = false )
    @ApiModelProperty(value = "属性分组Id")
    private String attrGroupTitle;

    @ApiModelProperty(value = "属性Id")
    private String attrId;

    @TableField(exist = false )
    @ApiModelProperty(value = "属性Id")
    private String attrTitle;

    @ApiModelProperty(value = "逻辑删除，0=未删除，1=删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

}
