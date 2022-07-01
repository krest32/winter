package com.krest.vedio.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krest.utils.myexception.myException;
import com.krest.utils.utils.IdWorker;
import com.krest.vedio.entity.Album;
import com.krest.vedio.entity.Catelog;
import com.krest.vedio.entity.Vedio;
import com.krest.vedio.mapper.CatelogMapper;
import com.krest.vedio.service.AlbumService;
import com.krest.vedio.service.CatelogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.vedio.service.VedioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频分类表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-05
 */
@Service
public class CatelogServiceImpl extends ServiceImpl<CatelogMapper, Catelog> implements CatelogService {

    public static IdWorker idWorker = new IdWorker();

    @Autowired
    private AlbumService albumService;

    @Autowired
    private VedioService vedioService;

    @Override
    public void addCateLog(Catelog catelog) {
        catelog.setId(idWorker.nextId());
        baseMapper.insert(catelog);
    }

    @Override
    public void deleteCatelog(String id) {
        QueryWrapper<Album>  albumQueryWrapper = new QueryWrapper<>();
        albumQueryWrapper.eq("catelog_id",id);
        int count = albumService.count(albumQueryWrapper);

        if (count>0){
            throw new myException(20001,"该分类正在被某个专辑使用，无法删除");
        }

        QueryWrapper<Vedio> vedioQueryWrapper = new QueryWrapper<>();
        vedioQueryWrapper.eq("catelog_id",id);
        int count1 = vedioService.count(vedioQueryWrapper);

        if (count1>0){
            throw new myException(20001,"该分类正在被某个专辑使用，无法删除");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public List<Catelog> listCateLog() {
        List<Catelog> catelogList= baseMapper.selectList(null);

        List<Catelog> collect = catelogList.stream().map(catelog -> {
            return editCatrelog(catelog);
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public Catelog getCatelogById(String id) {
        Catelog catelog = editCatrelog(baseMapper.selectById(id));
        return catelog;
    }

    private Catelog editCatrelog(Catelog catelog) {
        // 获取专辑数量
        String catelogId = catelog.getId();
        QueryWrapper<Album>  albumQueryWrapper = new QueryWrapper<>();
        albumQueryWrapper.eq("catelog_id",catelogId);
        int albumCount = albumService.count(albumQueryWrapper);
        catelog.setAlbumNum(albumCount);

        // 获取专辑列表
        List<Album> list = albumService.list(albumQueryWrapper);
        catelog.setAlbumList(list);

        // 获取视频数量
        QueryWrapper<Vedio> vedioQueryWrapper = new QueryWrapper<>();
        vedioQueryWrapper.eq("catelog_id",catelogId);
        int vedioCount = vedioService.count(vedioQueryWrapper);
        catelog.setVedioNum(vedioCount);

        return catelog;
    }
}
