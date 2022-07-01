package com.krest.others.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/11/13 11:00
 * @Description: ES 关系映射信息
 */
@Data
public class EsModel {

    private String blogId;

    private String blogTitle;

    private String blogSortId;

    private String blogSrotImg;

    private String blogimg;

    private BigDecimal blogPrice;

    private Integer openComment;

    private Long hotScore;

    private List<Attrs> attrs;


    @Data
    public static class Attrs{
        private Long attrId;
        private String attrName;
        private String attrValue;
    }

}
