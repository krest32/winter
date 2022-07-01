package com.krest.member.service;

import com.krest.member.entity.VedioCollect;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员视频收藏表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
public interface VedioCollectService extends IService<VedioCollect> {

    void addCollectVedio(VedioCollect vedioCollect);
}
