package com.krest.others.entity;

import lombok.Data;

import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/11/19 19:20
 * @Description:
 */
@Data
public class SearchVo {

    private String keyWords;

    // 品牌可以是多选
    private List<String> BrandId;


    private String threeSortId;

    private String twoSortId;

    private String oneSortId;

    private Boolean hasStock=true;

    private String id;

    private String Price;

    private List<String> attrSearch;

    /**
     * 因为数据量后期可能很大，所以加入页码
     */
    private Long pageNumber=1L ;

    private Long sizeNumber=5L;

}
