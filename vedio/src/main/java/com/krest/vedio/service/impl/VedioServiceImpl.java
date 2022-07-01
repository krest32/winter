package com.krest.vedio.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krest.member.entity.Vo.QueryVedio;
import com.krest.utils.myexception.myException;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import com.krest.vedio.client.AliyunClient;
import com.krest.vedio.client.VedioCollect;
import com.krest.vedio.entity.Album;
import com.krest.vedio.entity.Catelog;
import com.krest.vedio.entity.Picture;
import com.krest.vedio.entity.Vedio;
import com.krest.vedio.entity.vo.QueryVedioVo;
import com.krest.vedio.mapper.VedioMapper;
import com.krest.vedio.service.AlbumService;
import com.krest.vedio.service.CatelogService;
import com.krest.vedio.service.PictureService;
import com.krest.vedio.service.VedioService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.netflix.client.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频信息表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-05
 */
@Service
public class VedioServiceImpl extends ServiceImpl<VedioMapper, Vedio> implements VedioService {

    public static IdWorker idWorker = new IdWorker();

    @Autowired
    private AlbumService albumService;

    @Autowired
    private CatelogService catelogService;

    @Autowired
    private AliyunClient aliyunClient;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private VedioCollect vedioCollect;

    @Override
    public R querySearchVedio(Long page, Long limit, QueryVedioVo queryVedioVo) {
        QueryWrapper<Vedio> vedioQueryWrapper = new QueryWrapper<>();
        Page<Vedio> vedioPage = new Page<>(page,limit);

        String title = queryVedioVo.getTitle();
        String albumId = queryVedioVo.getAlbumId();
        String catelogId = queryVedioVo.getCatelogId();

        if(!StringUtils.isEmpty(title)){
            vedioQueryWrapper.like("title",title);
        }
        if (!StringUtils.isEmpty(albumId)){
            vedioQueryWrapper.eq("album_id",albumId);
        }
        if(!StringUtils.isEmpty(catelogId)){
            vedioQueryWrapper.eq("catelog_id",catelogId);
        }

        IPage<Vedio> vedioIPage = baseMapper.selectPage(vedioPage, vedioQueryWrapper);
        Long total = vedioIPage.getTotal();
        List<Vedio> records = vedioIPage.getRecords();

        List<Vedio> collect = records.stream().map(record->{
            return editVedioInfo(record);
        }).collect(Collectors.toList());

        return R.ok().data("total",total).data("vedioList",collect);
    }

    @Override
    public Vedio getVedio(String id) {
        Vedio vedio = baseMapper.selectById(id);
        Vedio vedioInfo = editVedioInfo(vedio);
        return vedioInfo;
    }

    @Override
    public void addVedio(Vedio vedio) {
        vedio.setId(idWorker.nextId());
        BigDecimal price = vedio.getPrice();
        double v = price.doubleValue();
        if(v > 0){
            vedio.setIsFree(false);
        }else {
            vedio.setIsFree(true);
        }

        // 保存新的图片信息
        Picture picture = new Picture();
        String pictureId = idWorker.nextId();

        //执行判断语句
        String pictureName = vedio.getPictureName();
        if (StringUtils.isEmpty(pictureName)){
            aliyunClient.removeVideo(vedio.getVideoSourceId());
            throw new myException(20001,",没有上传视频封面");
        }
        vedio.setPictureId(pictureId);
        picture.setId(pictureId);
        picture.setAddress(vedio.getPictureAddress());
        picture.setFilename(vedio.getPictureName());
        pictureService.save(picture);

        baseMapper.insert(vedio);
    }

    @Override
    public void deleteVedio(String id) {
        Vedio vedio = baseMapper.selectById(id);
        String videoSourceId = vedio.getVideoSourceId();

        //删除视频
        aliyunClient.removeVideo(videoSourceId);

        //删除图片信息
        String pictureId = vedio.getPictureId();
        Picture picture = pictureService.getById(pictureId);
        aliyunClient.deleteOss(picture.getFilename());
        pictureService.removeById(pictureId);

        //删除vedio信息
        baseMapper.deleteById(id);
    }

    @Override
    public void updateVedio(Vedio vedio) {
        // 校验是否免费
        BigDecimal price = vedio.getPrice();
        double v = price.doubleValue();
        if(v > 0){
            vedio.setIsFree(false);
        }else {
            vedio.setIsFree(true);
        }

        // 校验是否否上传新的视频
        String vedioId = vedio.getId();
        Vedio selectById = baseMapper.selectById(vedioId);
        String videoSourceId = selectById.getVideoSourceId();
        String sourceId = vedio.getVideoSourceId();
        if (!(sourceId.equals(videoSourceId))){
            aliyunClient.removeVideo(videoSourceId);
        }

        // 校验图片是否更新
        String pictureId = selectById.getPictureId();
        Picture picture = pictureService.getById(pictureId);
        String filename = picture.getFilename();
        String pictureName = vedio.getPictureName();
        if (!(pictureName.equals(filename))){
            aliyunClient.deleteOss(filename);
            picture.setFilename(vedio.getPictureName());
            picture.setAddress(vedio.getPictureAddress());
            pictureService.updateById(picture);
        }
        baseMapper.updateById(vedio);
    }

    /**
     * 根据播放量获取视频
     */
    @Override
    public R vedioListOrderByPlayCount() {
        QueryWrapper<Vedio> vedioQueryWrapper = new QueryWrapper<>();
        vedioQueryWrapper.orderByDesc("click_count");
        vedioQueryWrapper.last("limit 12");

        List<Vedio> vediodList = baseMapper.selectList(vedioQueryWrapper);

        List<Vedio> collect = vediodList.stream().map(vedio -> {
            return editVedioInfo(vedio);
        }).collect(Collectors.toList());

        return R.ok().data("vedioList",collect);
    }

    @Override
    public R editClickVedio(String id) throws ClientException {
        // 添加点击数
        Vedio vedio = baseMapper.selectById(id);
        Long clickCount = vedio.getClickCount();
        clickCount=clickCount+1;
        vedio.setClickCount(clickCount);

        // 更新视频信息
        baseMapper.updateById(vedio);

        //获取视频id
        String videoSourceId = vedio.getVideoSourceId();


        return aliyunClient.getPlayAuth(videoSourceId).data("videoSourceId",videoSourceId);
    }

    @Override
    public R getVedioListById(String id, Long page, Long limit, QueryVedioVo queryVedioVo) {

        // 得到专辑信息
        Album album = albumService.getAlbumById(id);
        String pictureId = album.getPictureId();
        Picture albumPicture = pictureService.getById(pictureId);
        album.setPictureAddress(albumPicture.getAddress());
        album.setPictureName(albumPicture.getFilename());

        // 得到专辑下的视频列表
        QueryWrapper<Vedio> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("album_id",id);
        Page<Vedio> vedioPage = new Page<>(page,limit);
        IPage<Vedio> vedioIPage = baseMapper.selectPage(vedioPage, queryWrapper);
        Long total = vedioIPage.getTotal();
        List<Vedio> records = vedioIPage.getRecords();

        List<Vedio> collect = records.stream().map(record->{
            return editVedioInfo(record);
        }).collect(Collectors.toList());

        String albumTitle = collect.get(0).getAlbumTitle();
        return R.ok().data("total",total).data("vedioList",collect).data("albumTitle",albumTitle).data("albumInbfo",album);
    }

    @Override
    public List<Vedio> getMemberVedioList(QueryVedio queryVedio) {
        List<String> vedioIdList = queryVedio.getVedioIdList();
        List<Vedio> vedioList = baseMapper.selectBatchIds(vedioIdList);
        List<Vedio> collect = vedioList.stream().map(vedio -> {
            return editVedioInfo(vedio);
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public R getVedioListByVedioId(String id, Long page, Long limit, QueryVedioVo queryVedioVo) {
        Vedio vedio = baseMapper.selectById(id);
        String albumId = vedio.getAlbumId();

        // 得到专辑信息
        Album album = albumService.getById(albumId);
        String pictureId = album.getPictureId();
        Picture albumPicture = pictureService.getById(pictureId);
        album.setPictureAddress(albumPicture.getAddress());
        album.setPictureName(albumPicture.getFilename());

        // 得到专辑下的视频列表
        QueryWrapper<Vedio> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("album_id",albumId);
        Page<Vedio> vedioPage = new Page<>(page,limit);
        IPage<Vedio> vedioIPage = baseMapper.selectPage(vedioPage, queryWrapper);
        Long total = vedioIPage.getTotal();
        List<Vedio> records = vedioIPage.getRecords();

        List<Vedio> collect = records.stream().map(record->{
            return editVedioInfo(record);
        }).collect(Collectors.toList());

        String albumTitle = album.getTitle();
        return R.ok().data("total",total).data("vedioList",collect).data("albumTitle",albumTitle).data("albumInbfo",album);
    }


    private Vedio editVedioInfo(Vedio record) {
        String albumId = record.getAlbumId();
        String catelogId = record.getCatelogId();

        Long size = record.getSize();
        Long vediosize = size/1024/1024;
        record.setVedioSize(vediosize+" MB");

        Album albumById = albumService.getAlbumById(albumId);
        record.setAlbumTitle(albumById.getTitle());

        Catelog catelog = catelogService.getById(catelogId);
        record.setCatelogTitle(catelog.getTitle());


//        String vedioId = record.getId();
//        int i = vedioCollect.countCollection(vedioId);
//        record.setCollectCount(i);

        //获取图片信息
        String pictureId = record.getPictureId();
        Picture byId = pictureService.getById(pictureId);
        record.setPictureName(byId.getFilename());
        record.setPictureAddress(byId.getAddress());

        return record;
    }
}
