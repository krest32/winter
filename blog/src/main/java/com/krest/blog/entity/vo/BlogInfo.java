package com.krest.blog.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: krest
 * @Date: 2020/12/4 15:46
 * @Description:
 */
@Data
public class BlogInfo {
    @ApiModelProperty(value = "blog主键ID")
    private String id;

    @ApiModelProperty(value = "博客标题")
    private String title;

    @ApiModelProperty(value = "博客简介")
    private String summary;

    @ApiModelProperty(value = "博客内容")
    private String content;

    @ApiModelProperty(value = "标签")
    private String tag;

    @ApiModelProperty(value = "标签")
    private String tagId;

    @ApiModelProperty(value = "图片id")
    private String pictureUid;

    @ApiModelProperty(value = "图片")
    private String avatar;

    @ApiModelProperty(value = "图片名字")
    private String pictureFileName;

    @ApiModelProperty(value = "状态，0=草稿，1=发布")
    private Integer status;

    @ApiModelProperty(value = "作者")
    private String authorName;

    @ApiModelProperty(value = "作者Id")
    private String authorId;

    @ApiModelProperty(value = "博客分类ID")
    private String blogSortId;

    @ApiModelProperty(value = "博客分类")
    private String blogSort;

    @ApiModelProperty(value = "分类图片")
    private String blogSortPicture;

    @ApiModelProperty(value = "销售价格，设置为0则可免费观看")
    private BigDecimal price;

    @ApiModelProperty(value = "是否开启评论(0:否 1:是)")
    private Integer openComment;

    @ApiModelProperty(value = "够买次数")
    private Long buyCount;

    @ApiModelProperty(value = "点击次数")
    private Long clickCount;

    @ApiModelProperty(value = "收藏次数")
    private Long collectCount;

    @ApiModelProperty(value = "逻辑删除，0=未删除，1=删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private Date gmtModified;

}
