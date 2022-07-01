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
 * 品牌描述
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product_brand")
@ApiModel(value="Brand对象", description="品牌描述")
public class Brand implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "brand主键ID")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "名称")
    private String title;

    @ApiModelProperty(value = "介绍")
    private String intro;

    @ApiModelProperty(value = "属性图片")
    private String brandImage;

    @ApiModelProperty(value = "属性图片名称")
    private String brandImageFilename;

    @TableField(exist = false)
    @ApiModelProperty(value = "产品数量")
    private Integer productNum;

    @ApiModelProperty(value = "逻辑删除，0=未删除，1=删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
