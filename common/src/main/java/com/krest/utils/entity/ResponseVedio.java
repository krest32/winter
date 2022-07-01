package com.krest.utils.entity;

import lombok.Data;

import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/12/11 17:39
 * @Description:
 */
@Data
public class ResponseVedio {
    private Long total;
    private List<Vedio> vedioList;
}
