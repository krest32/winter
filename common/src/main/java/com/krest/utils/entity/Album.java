package com.krest.utils.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 专辑表
 * </p>
 *
 * @author krest
 * @since 2020-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vedio_album")
@ApiModel(value="Album对象", description="专辑表")
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "专辑ID")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "分类Id")
    private String catelogId;

    @TableField(exist = false)
    @ApiModelProperty(value = "分类标题")
    private String catelogTitle;

    @TableField(exist = false)
    @ApiModelProperty(value = "视频熟练")
    private Integer vedioNum;

    @ApiModelProperty(value = "专辑名称")
    private String title;

    @ApiModelProperty(value = "专辑简介")
    private String intro;

    @ApiModelProperty(value = "图片Id")
    private String pictureId;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片名称")
    private String pictureName;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片地址")
    private String pictureAddress;


    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "购买人数")
    private Long puyCount;

    @ApiModelProperty(value = "收藏人数")
    private Long collectCount;

    @ApiModelProperty(value = "点击人数")
    private Long clickCount;

    @ApiModelProperty(value = "播放次数")
    private Long playCount;

    @ApiModelProperty(value = "是否可以试听：0收费 1免费")
    private Boolean isFree;

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
