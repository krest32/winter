package com.krest.product.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 分类属性分组表
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product_catelog_attrgroup")
@ApiModel(value="CatelogAttrgroup对象", description="分类属性分组表")
public class CatelogAttrgroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "属性分组ID")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "属性名称")
    private String title;

    @ApiModelProperty(value = "属性所属分类ID")
    private String oneSortId;

    @ApiModelProperty(value = "逻辑删除，0=未删除，1=删除")
    private Boolean isDeleted;

    @TableField(exist = false)
    @ApiModelProperty(value = "Attr数量")
    private Integer attrNum;

    @TableField(exist = false)
    @ApiModelProperty(value = "Attr数量")
    private List<CatelogAttr> attrList;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
