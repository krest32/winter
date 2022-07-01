package com.krest.vedio.entity;

import java.math.BigDecimal;

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
 * 视频信息表
 * </p>
 *
 * @author krest
 * @since 2020-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Vedio对象", description="视频信息表")
public class Vedio implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "视频ID")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "专辑Id")
    private String albumId;

    @TableField(exist = false)
    @ApiModelProperty(value = "专辑名称")
    private String albumTitle;

    @TableField(exist = false)
    @ApiModelProperty(value = "专辑名称")
    private Album albumInfo;

    @ApiModelProperty(value = "分类Id")
    private String catelogId;

    @TableField(exist = false)
    @ApiModelProperty(value = "分类名称")
    private String catelogTitle;

    @ApiModelProperty(value = "图片Id")
    private String pictureId;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片名称")
    private String pictureName;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片地址")
    private String pictureAddress;

    @ApiModelProperty(value = "视频名称")
    private String title;

    @ApiModelProperty(value = "云端视频资源")
    private String videoSourceId;

    @ApiModelProperty(value = "原始文件名称")
    private String videoOriginalName;

    @ApiModelProperty(value = "播放次数")
    private Long playCount;

    @ApiModelProperty(value = "是否可以试听：0收费 1免费")
    private Boolean isFree;

    @ApiModelProperty(value = "视频时长（秒）")
    private Float duration;

    @ApiModelProperty(value = "视频源文件大小（字节）")
    private Long size;

    @TableField(exist = false)
    @ApiModelProperty(value = "视频文件大小（mb）")
    private String vedioSize;

    @ApiModelProperty(value = "销售价格，设置为0则可免费观看")
    private BigDecimal price;

    @ApiModelProperty(value = "购买人数")
    private Long puyCount;

    @ApiModelProperty(value = "收藏人数")
    private Integer collectCount;

    @ApiModelProperty(value = "点击人数")
    private Long clickCount;

    @ApiModelProperty(value = "乐观锁")
    private Long version;

    @ApiModelProperty(value = "逻辑删除，0=未删除，1=删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
