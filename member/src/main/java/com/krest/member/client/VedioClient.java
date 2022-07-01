package com.krest.member.client;

import com.krest.member.entity.Vo.QueryVedio;
import com.krest.utils.entity.Album;
import com.krest.utils.entity.Vedio;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Auther: krest
 * @Date: 2020/12/11 17:32
 * @Description:
 */
@Component
@FeignClient("vedio")
public interface VedioClient {

    @ApiOperation(value = "Web: 根据member到vedioList")
    @PostMapping("/vedio/vedio/getMemberVedioList")
    public List<Vedio> getMemberVedioList(@RequestBody QueryVedio queryVedio);

    @ApiOperation(value = "Client：根据id得到Album")
    @GetMapping("/vedio/album/clientGetAlbumById/{id}")
    public Album clientGetAlbumById(@PathVariable("id") String id);

}
