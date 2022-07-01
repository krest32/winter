package com.krest.product.service;

import com.krest.product.entity.Catelog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 三级分类关系表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-12
 */
public interface CatelogService extends IService<Catelog> {

    List<Catelog> GetAllCatelog();

    List<Catelog> listCateLogThree();
}
