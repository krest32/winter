package com.krest.vedio.service;

import com.krest.member.entity.Vo.QueryVedio;
import com.krest.utils.response.R;
import com.krest.vedio.entity.Vedio;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krest.vedio.entity.vo.QueryVedioVo;
import com.netflix.client.ClientException;

import java.util.List;

/**
 * <p>
 * 视频信息表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-05
 */
public interface VedioService extends IService<Vedio> {

    R querySearchVedio(Long page, Long limit, QueryVedioVo queryVedioVo);

    Vedio getVedio(String id);

    void addVedio(Vedio vedio);

    void deleteVedio(String id);

    void updateVedio(Vedio vedio);

    R vedioListOrderByPlayCount();

    R editClickVedio(String id) throws ClientException;

    R getVedioListById(String id, Long page, Long limit, QueryVedioVo queryVedioVo);

    List<Vedio> getMemberVedioList(QueryVedio queryVedio);

    R getVedioListByVedioId(String id, Long page, Long limit, QueryVedioVo queryVedioVo);
}
