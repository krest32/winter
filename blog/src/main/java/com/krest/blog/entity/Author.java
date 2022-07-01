package com.krest.blog.entity;

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
 * 作者信息表
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("blog_author")
@ApiModel(value="Author对象", description="作者信息表")
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "author主键ID")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "作者名字")
    private String name;

    @ApiModelProperty(value = "性别，0=男，1=女")
    private Integer sex;

    @ApiModelProperty(value = "一句话简介")
    private String intro;

    @ApiModelProperty(value = "作者详细介绍")
    private String summary;

    @ApiModelProperty(value = "作者头像图片Id")
    private String pictureUid;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片名称")
    private String pictureName;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片地址")
    private String pictureAddress;

    @TableField(exist = false)
    @ApiModelProperty(value = "发表Blog数量")
    private Integer publishBlogNum;

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
