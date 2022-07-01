package com.krest.vedio.controller;


import com.krest.utils.response.R;
import com.krest.vedio.entity.Album;
import com.krest.vedio.entity.Catelog;
import com.krest.vedio.entity.vo.QueryAlbumVo;
import com.krest.vedio.service.AlbumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 专辑表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-05
 */
@CrossOrigin
@Api(value = "专辑管理",tags ="专辑管理")
@RestController
@RequestMapping("/vedio/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @ApiOperation(value = "条件查询所有的Album")
    @PostMapping("pageQueryAlbum/{page}/{limit}")
    public R pageQueryAlbum(@PathVariable Long page,
                            @PathVariable Long limit,
                            @RequestBody(required = false) QueryAlbumVo queryAlbumVo){
        return albumService.pageQueryAlbum(page,limit,queryAlbumVo);
    }


    @ApiOperation(value = "列出所有的Album")
    @GetMapping("listAlbum")
    public R listAlbum(){
        List<Album> list = albumService.listAlbum();
        return R.ok().data("albumList",list);
    }

    @ApiOperation(value = "添加新的Album")
    @PostMapping("addAlbum")
    public R addAlbum(@RequestBody Album album){
        albumService.addAlbum(album);
        return R.ok();
    }

    @ApiOperation(value = "根据id删除Album")
    @DeleteMapping("deleteAlbum/{id}")
    public R deleteAlbum(@PathVariable String id){
        albumService.deleteAlbum(id);
        return R.ok();
    }

    @ApiOperation(value = "根据id得到Album")
    @GetMapping("getAlbumById/{id}")
    public R getAlbumById(@PathVariable String id){
        Album album = albumService.getAlbumById(id);
        return R.ok().data("album",album);
    }


    @ApiOperation(value = "根据id更新Album")
    @PostMapping("updateAlbumById")
    public R updateAlbumById(@RequestBody Album album){
        albumService.updateAlbumById(album);
        return R.ok();
    }



    @ApiOperation(value = "Client：根据id得到Album")
    @GetMapping("clientGetAlbumById/{id}")
    public Album clientGetAlbumById(@PathVariable String id){
        Album album = albumService.getAlbumById(id);
        return album;
    }
}

