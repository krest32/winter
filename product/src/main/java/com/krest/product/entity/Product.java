package com.krest.product.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;
import java.util.List;

import com.krest.product.entity.vo.ProductAttrVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品信息表
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Product对象", description="商品信息表")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品Id")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "商品名称")
    private String title;

    @ApiModelProperty(value = "品牌Id")
    private String brandId;

    @TableField(exist = false)
    @ApiModelProperty(value = "品牌名称")
    private String brandTitle;

    @ApiModelProperty(value = "品牌Id")
    private String oneSortId;

    @TableField(exist = false)
    @ApiModelProperty(value = "一级分类名称")
    private String oneSortTitle;

    @TableField(exist = false)
    @ApiModelProperty(value = "二级分类Id")
    private String twoSortId;

    @TableField(exist = false)
    @ApiModelProperty(value = "二级分类名称")
    private String twoSortTitle;

    @TableField(exist = false)
    @ApiModelProperty(value = "三级分类Id")
    private String threeSortId;

    @TableField(exist = false)
    @ApiModelProperty(value = "三级分类名称")
    private String threeSortTitle;

    @TableField(exist = false)
    @ApiModelProperty(value = "分类ID集合")
    private List<String> sortIdList;

    @ApiModelProperty(value = "商品图片")
    private String picture;

    @ApiModelProperty(value = "商品图片名称")
    private String pictureName;

    @TableField(exist = false)
    @ApiModelProperty(value = "分组属性Id合集")
    private List<String> attrGroupIdList;

    @TableField(exist = false)
    @ApiModelProperty(value = "分组属性信息合集")
    private List<ProductAttrVo> productAttrList;


    @TableField(exist = false)
    @ApiModelProperty(value = "属性Id合集")
    private List<String> attrIdList;

    @TableField(exist = false)
    @ApiModelProperty(value = "属性合集")
    private List<Attr> attrList;

    @ApiModelProperty(value = "销售价格")
    private BigDecimal price;

    @ApiModelProperty(value = "状态，0=下架，1=上架")
    private Boolean status;

    @ApiModelProperty(value = "逻辑删除，0=未删除，1=删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
