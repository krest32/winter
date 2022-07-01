package com.krest.blog.entity;

import java.math.BigDecimal;

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
 * 博客表
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Blog对象", description="博客表")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "blog主键ID")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "博客标题")
    private String title;

    @ApiModelProperty(value = "博客简介")
    private String summary;

    @ApiModelProperty(value = "博客内容")
    private String content;

    @ApiModelProperty(value = "标题图片id")
    private String pictureUid;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片名称")
    private String pictureName;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片地址")
    private String pictureAddress;

    @ApiModelProperty(value = "状态，0=草稿，1=发布")
    private Boolean status;

    @ApiModelProperty(value = "作者id")
    private String authorId;

    @ApiModelProperty(value = "作者id")
    @TableField(exist = false)
    private String authorName;

    @ApiModelProperty(value = "作者id")
    @TableField(exist = false)
    private String authorAvatar;

    @ApiModelProperty(value = "作者id")
    @TableField(exist = false)
    private String authorSummary;

    @ApiModelProperty(value = "博客分类ID")
    private String blogSortId;

    @ApiModelProperty(value = "博客分类标题")
    @TableField(exist = false)
    private String blogSortTitle;

    @ApiModelProperty(value = "博客分类ID")
    @TableField(exist = false)
    private List<String> tagIdList;

    @ApiModelProperty(value = "博客分类ID")
    @TableField(exist = false)
    private List<Tag> tagList;

    @ApiModelProperty(value = "销售价格，设置为0则可免费观看")
    private BigDecimal price;

    @ApiModelProperty(value = "是否开启评论(0:否 1:是)")
    private Integer openComment;

    @ApiModelProperty(value = "购买人数")
    private Long puyCount;

    @ApiModelProperty(value = "收藏人数")
    private Long collectCount;

    @ApiModelProperty(value = "点击人数")
    private Long clickCount;

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
