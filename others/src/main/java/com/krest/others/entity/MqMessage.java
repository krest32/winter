package com.krest.others.entity;

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
 * MQ消息状态表
 * </p>
 *
 * @author krest
 * @since 2020-12-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("others_mq_message")
@ApiModel(value="MqMessage对象", description="MQ消息状态表")
public class MqMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息ID")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "交换机")
    private String toExchage;

    @ApiModelProperty(value = "路由")
    private String routingKey;

    private String classType;

    @ApiModelProperty(value = "0 新建 1 发送 2 错误抵达 3 已经抵达")
    private Integer messageStatus;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
