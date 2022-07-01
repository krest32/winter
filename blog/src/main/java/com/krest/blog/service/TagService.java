package com.krest.blog.service;

import com.krest.blog.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
public interface TagService extends IService<Tag> {

    Integer judge(String id);

    List<Tag> listAllTag(Object o);
}
