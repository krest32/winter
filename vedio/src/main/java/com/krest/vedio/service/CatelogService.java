package com.krest.vedio.service;

import com.krest.vedio.entity.Catelog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 视频分类表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-05
 */
public interface CatelogService extends IService<Catelog> {

    void addCateLog(Catelog catelog);

    void deleteCatelog(String id);

    List<Catelog> listCateLog();

    Catelog getCatelogById(String id);
}
