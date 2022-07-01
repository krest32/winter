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
 * 三级分类关系表
 * </p>
 *
 * @author krest
 * @since 2020-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product_catelog")
@ApiModel(value="Catelog对象", description="三级分类关系表")
public class Catelog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类主键ID")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "分类名称")
    private String title;

    @ApiModelProperty(value = "父类ID,一级分类的付类Id为0")
    private String parentId;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品数量")
    private int productNum = 0;


    @TableField(exist = false)
    private List<Catelog> childrenCatelogs;

    @ApiModelProperty(value = "逻辑删除，0=未删除，1=删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
