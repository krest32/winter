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
 * 属性描述表
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product_catelog_attr")
@ApiModel(value="CatelogAttr对象", description="属性描述表")
public class CatelogAttr implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "属性描述ID")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "属性描述内容")
    private String title;

    @ApiModelProperty(value = "属性分组id")
    private String attrGroupId;

    @TableField(exist = false)
    @ApiModelProperty(value = "属性使用数量")
    private Integer attrUseNum;

    @ApiModelProperty(value = "逻辑删除，0=未删除，1=删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
