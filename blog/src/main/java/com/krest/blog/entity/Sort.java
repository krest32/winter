package com.krest.blog.entity;

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
 * blog分类表
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("blog_sort")
@ApiModel(value="Sort对象", description="blog分类表")
public class Sort implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "blog分类名称")
    private String title;

    @ApiModelProperty(value = "分类简介")
    private String content;

    @ApiModelProperty(value = "分类图片Id")
    private String pictureUid;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片名称")
    private String pictureName;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片地址")
    private String pictureAddress;

    @TableField(exist = false)
    @ApiModelProperty(value = "博客数量")
    private Integer blogNum;

    @TableField(exist = false)
    @ApiModelProperty(value = "博客数量权重占比")
    private Integer blogNumWeight;

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
