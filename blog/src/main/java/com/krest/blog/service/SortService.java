package com.krest.blog.service;

import com.krest.blog.entity.Sort;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krest.blog.entity.vo.QuerySortVo;
import com.krest.utils.response.R;

/**
 * <p>
 * blog分类表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
public interface SortService extends IService<Sort> {

    void addSort(Sort sort);

    R PageQuerySort(Long page, Long limit, QuerySortVo querySortVo);

    Sort getSortById(String id);

    void updateSort(Sort sort);

    void deleteSort(String id);
}
