package com.krest.vedio.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krest.utils.myexception.myException;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import com.krest.vedio.client.AliyunClient;
import com.krest.vedio.entity.Album;
import com.krest.vedio.entity.Catelog;
import com.krest.vedio.entity.Picture;
import com.krest.vedio.entity.Vedio;
import com.krest.vedio.entity.vo.QueryAlbumVo;
import com.krest.vedio.mapper.AlbumMapper;
import com.krest.vedio.service.AlbumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.vedio.service.CatelogService;
import com.krest.vedio.service.PictureService;
import com.krest.vedio.service.VedioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 专辑表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-05
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements AlbumService {

    public static IdWorker idWorker = new IdWorker();

    @Autowired
    private CatelogService catelogService;

    @Autowired
    private VedioService vedioService;

    @Autowired
    private AliyunClient aliyunClient;

    @Autowired
    private PictureService pictureService;

    @Override
    public R pageQueryAlbum(Long page, Long limit, QueryAlbumVo queryAlbumVo) {
        QueryWrapper<Album> queryWrapper = new QueryWrapper<>();
        Page<Album> albumPage = new Page<>(page,limit);

        String title = queryAlbumVo.getTitle();
        String catelogId = queryAlbumVo.getCatelogId();

        if(!StringUtils.isEmpty(title)){
            queryWrapper.like("title",title);
        }
        if (!StringUtils.isEmpty(catelogId)){
            queryWrapper.eq("catelog_id",catelogId);
        }

        IPage<Album> albumIPage = baseMapper.selectPage(albumPage, queryWrapper);
        Long total = albumIPage.getTotal();
        List<Album> records = albumIPage.getRecords();

        List<Catelog> catelogList = catelogService.list(null);

        List<Album> collect = records.stream().map(record->{
            return editAlbum(record,catelogList);
        }).collect(Collectors.toList());


        return R.ok().data("total",total).data("albumList",collect);
    }

    @Override
    public void addAlbum(Album album) {
        // 处理图片信息
        String pictureId = idWorker.nextId();
        Picture picture = new Picture();
        picture.setId(pictureId);
        picture.setFilename(album.getPictureName());
        picture.setAddress(album.getPictureAddress());
        pictureService.save(picture);

        // 处理Album信息
        album.setId(idWorker.nextId());
        album.setPictureId(pictureId);
        baseMapper.insert(album);
    }

    @Override
    public void deleteAlbum(String id) {
        Album album = baseMapper.selectById(id);
        String pictureId = album.getPictureId();

        //判断ablum下是否有vedio
        QueryWrapper<Vedio> vedioQueryWrapper = new QueryWrapper<>();
        vedioQueryWrapper.eq("album_id",id);
        int count = vedioService.count(vedioQueryWrapper);
        if (count>0){
            throw new myException(20001,"专辑下有vedio,无法删除");
        }else {
            // 处理图片
            Picture picture = pictureService.getById(pictureId);
            String filename = picture.getFilename();
            aliyunClient.deleteOss(filename);
            pictureService.removeById(pictureId);
            // 删除album
            baseMapper.deleteById(id);
        }
    }

    @Override
    public Album getAlbumById(String id) {
        List<Catelog> catelogList = catelogService.list(null);
        Album record = baseMapper.selectById(id);
        Album album= editAlbum(record, catelogList);
        return album;
    }

    @Override
    public void updateAlbumById(Album album) {
        // 校验图片是狗更新
        String pictureId = album.getPictureId();
        Picture picture = pictureService.getById(pictureId);
        String filename = picture.getFilename();
        String pictureName = album.getPictureName();
        if (!filename.equals(pictureName)){
            aliyunClient.deleteOss(filename);
            picture.setFilename(pictureName);
            picture.setAddress(album.getPictureAddress());
            pictureService.updateById(picture);
        }

        // 保存新的album
        baseMapper.updateById(album);
    }

    @Override
    public List<Album> listAlbum() {
        List<Album> albumList = baseMapper.selectList(null);
        return albumList;
    }

    private Album editAlbum(Album record,List<Catelog> catelogList) {
        //设置catelogTitle
        for (Catelog catelog : catelogList) {
            String id = catelog.getId();
            if (id.equals(record.getCatelogId())){
               record.setCatelogTitle(catelog.getTitle());
            }
        }

        //获取视频数量
        QueryWrapper<Vedio> vedioQueryWrapper = new QueryWrapper<>();
        vedioQueryWrapper.eq("album_id",record.getId());
        int count = vedioService.count(vedioQueryWrapper);
        record.setVedioNum(count);

        //添加图片信息
        String pictureId = record.getPictureId();
        Picture picture = pictureService.getById(pictureId);
        record.setPictureName(picture.getFilename());
        record.setPictureAddress(picture.getAddress());
        return record;


    }
}
