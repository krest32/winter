package com.krest.others.entity;

import com.krest.utils.entity.EsProductModel;
import lombok.Data;

import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/11/19 19:29
 * @Description:
 */
@Data
public class ResultVo {

    //查询得到的所有商品信息

    private List<EsProductModel> products;

    //以下是分页信息

    private Long pageNum;
    private Long total;
    private Long totalPages;

    //返回的分类信息

    private List<BrandVo> brandList;

    //提交一个一级或者二级分类

    private List<CataLogVo> CataLogList;

    //所有属性列表
    private List<attrVo> attrList;


    //展示返回的所有品牌数据
    @Data
    public static class BrandVo{
        private String brandId;
        private String brandTitle;
    }

    //所有属性的数据
    @Data
    public static class attrVo{
        private String attrGroupId;
        private String attrGroupTitle;
        //属性值可能有很多，所以用list封装
        private List<String> attrTitleList;
    }

    //所有分类的信息
    @Data
    public static class CataLogVo{
        private String cateLogId;
        private String cateLogTtitle;
    }
}
