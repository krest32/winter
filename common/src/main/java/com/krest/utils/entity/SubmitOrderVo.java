package com.krest.utils.entity;

import lombok.Data;

/**
 * @Auther: krest
 * @Date: 2020/12/23 20:50
 * @Description:
 */
@Data
public class SubmitOrderVo {
    private MQOrder order;
    private Integer code;
}
