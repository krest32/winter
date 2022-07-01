package com.krest.others.service;

import com.krest.others.entity.SearchVo;
import com.krest.utils.entity.EsProductModel;
import com.krest.utils.response.R;

import java.io.IOException;

/**
 * @Auther: krest
 * @Date: 2020/11/13 11:51
 * @Description:
 */

public interface EsService {


    R saveProduct(EsProductModel esProductModel) throws IOException;

    R deleteProduct(String id) throws IOException;

    R searchProduct(Long page,Long limit,SearchVo searchVo) throws IOException;
}
