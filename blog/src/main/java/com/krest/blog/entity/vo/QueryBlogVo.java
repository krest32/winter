package com.krest.blog.entity.vo;

import lombok.Data;

/**
 * @Auther: krest
 * @Date: 2020/12/4 15:42
 * @Description:
 */
@Data
public class QueryBlogVo {

    private String title;

    private Integer status;

    private String authorId;

    private String blogSortId;


}
