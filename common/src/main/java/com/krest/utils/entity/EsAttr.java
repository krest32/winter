package com.krest.utils.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: krest
 * @Date: 2020/12/14 18:27
 * @Description:
 */
@Data
public class EsAttr {

        @ApiModelProperty(value = "属性描述ID")
        private String attrId;

        @ApiModelProperty(value = "属性描述内容")
        private String attrTitle;

        @ApiModelProperty(value = "属性分组id")
        private String attrGroupId;

        @ApiModelProperty(value = "属性分组名称")
        private String attrGroupTitle;
}
