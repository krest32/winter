package com.krest.others.entity;

import lombok.Data;

/**
 * @Auther: krest
 * @Date: 2020/11/20 13:21
 * @Description:
 */
@Data
public class UserInfoVO {
    private String memberId;
    private String userTempKey;
    private boolean tempUSer=true;
}
