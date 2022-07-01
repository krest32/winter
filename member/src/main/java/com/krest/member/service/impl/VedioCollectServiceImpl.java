package com.krest.member.service.impl;

import com.krest.member.entity.VedioCollect;
import com.krest.member.mapper.VedioCollectMapper;
import com.krest.member.service.VedioCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.utils.utils.IdWorker;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员视频收藏表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Service
public class VedioCollectServiceImpl extends ServiceImpl<VedioCollectMapper, VedioCollect> implements VedioCollectService {


    public static IdWorker idWorker = new IdWorker();

    @Override
    public void addCollectVedio(VedioCollect vedioCollect) {
        vedioCollect.setId(idWorker.nextId());
        baseMapper.insert(vedioCollect);

    }
}
