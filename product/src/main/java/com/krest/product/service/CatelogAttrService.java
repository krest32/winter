package com.krest.product.service;

import com.krest.product.entity.CatelogAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 属性描述表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
public interface CatelogAttrService extends IService<CatelogAttr> {

    List<CatelogAttr> getAttrByGroupId(String groupId);

    void addNewAttr(CatelogAttr catelogAttr);

    void deleteAttrById(String id);

    void updateAttr(CatelogAttr catelogAttr);
}
