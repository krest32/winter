package com.krest.utils.entity.mqvo;

import lombok.Data;

import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/12/24 19:53
 * @Description:
 */
@Data
public class StockLockedVo {
    private String id;
    private StockLockDetail stockLockDetail;
}
