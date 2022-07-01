package com.krest.blog.service;

import com.krest.blog.entity.Author;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krest.blog.entity.vo.QueryAuthorVo;
import com.krest.utils.response.R;

import java.util.List;

/**
 * <p>
 * 作者信息表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
public interface AuthorService extends IService<Author> {
    Author getAuthorById(String id);

    void saveAuthor(Author author);

    R PageQueryAuthor(Long page, Long limit, QueryAuthorVo queryAuthorVo);

    void updateAuthor(Author author);

    void deleteAuthor(String id);

    List<Author> webAuthorList();
}
