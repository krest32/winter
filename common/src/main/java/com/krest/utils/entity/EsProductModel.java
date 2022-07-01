package com.krest.utils.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/11/19 15:47
 * @Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EsProductModel implements Serializable {

    private String id;

    private String title;

    private String brandId;

    private String brandTitle;

    private String oneSortId;

    private String oneSortTitle;

    private String twoSortId;

    private String twoSortTitle;

    private String threeSortId;

    private String threeSortTitle;

    private String picture;

    private String pictureName;

    private BigDecimal price;

    private Boolean status;

    private Boolean hasStock;

    private Long stock;

    private List<EsAttr> attrList;



}
