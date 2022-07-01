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
 * 图片信息表
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("blog_picture")
@ApiModel(value="Picture对象", description="图片信息表")
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "picture主键ID")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "图片名称")
    private String filename;

    @ApiModelProperty(value = "picture地址")
    private String address;

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
