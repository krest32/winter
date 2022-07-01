package com.krest.others.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Auther: krest
 * @Date: 2020/11/12 23:32
 * @Description:
 */
@Data
public class UserRest {


    private String id;

    private String name;

    private Integer age;

    private Date date;

    private String location;

}
