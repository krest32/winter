package com.krest.product.service;

import com.krest.product.entity.CatelogAttrgroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krest.utils.response.R;

import java.util.List;

/**
 * <p>
 * 分类属性分组表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
public interface CatelogAttrgroupService extends IService<CatelogAttrgroup> {



    List<CatelogAttrgroup> GetAttrGroupById(String id);

    void addNewAttrGroup(CatelogAttrgroup catelogAttrgroup);

    R deleteAttrGroup(String id);
}
