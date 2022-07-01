package com.krest.blog.service.impl;

import com.krest.blog.entity.Picture;
import com.krest.blog.mapper.PictureMapper;
import com.krest.blog.service.PictureService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 图片信息表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements PictureService {

}
