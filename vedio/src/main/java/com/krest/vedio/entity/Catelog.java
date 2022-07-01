package com.krest.vedio.entity;

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
 * 视频分类表
 * </p>
 *
 * @author krest
 * @since 2020-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vedio_catelog")
@ApiModel(value="Catelog对象", description="视频分类表")
public class Catelog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "分类名称")
    private String title;

    @ApiModelProperty(value = "专辑数量")
    @TableField(exist = false)
    private Integer albumNum;

    @ApiModelProperty(value = "专辑列表")
    @TableField(exist = false)
    private List<Album> albumList;

    @ApiModelProperty(value = "视频数量")
    @TableField(exist = false)
    private Integer vedioNum;

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
