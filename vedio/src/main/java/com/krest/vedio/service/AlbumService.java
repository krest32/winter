package com.krest.vedio.service;

import com.krest.utils.response.R;
import com.krest.vedio.entity.Album;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krest.vedio.entity.vo.QueryAlbumVo;

import java.util.List;

/**
 * <p>
 * 专辑表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-05
 */
public interface AlbumService extends IService<Album> {


    R pageQueryAlbum(Long page, Long limit, QueryAlbumVo queryAlbumVo);

    void addAlbum(Album album);

    void deleteAlbum(String id);

    Album getAlbumById(String id);

    void updateAlbumById(Album album);

    List<Album> listAlbum();
}
