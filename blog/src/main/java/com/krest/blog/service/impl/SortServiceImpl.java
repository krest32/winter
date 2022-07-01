package com.krest.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krest.blog.client.AliyunClient;
import com.krest.blog.entity.Blog;
import com.krest.blog.entity.Picture;
import com.krest.blog.entity.Sort;
import com.krest.blog.entity.vo.QuerySortVo;
import com.krest.blog.mapper.SortMapper;
import com.krest.blog.service.BlogService;
import com.krest.blog.service.PictureService;
import com.krest.blog.service.SortService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.utils.myexception.myException;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * blog分类表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
@Service
public class SortServiceImpl extends ServiceImpl<SortMapper, Sort> implements SortService {

    public static IdWorker idWorker = new IdWorker();

    @Autowired
    private PictureService pictureService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private AliyunClient aliyunClient;

    @Override
    public void addSort(Sort sort) {
        String pictureName = sort.getPictureName();
        String pictureAddress = sort.getPictureAddress();
        if(StringUtils.isEmpty(pictureName) || StringUtils.isEmpty(pictureAddress)){
            throw new myException(20001,"图片信息为空，请上传图片");
        }else{
            //保存图片信息
            String pictureid = idWorker.nextId();
            Picture picture = new Picture();
            picture.setId(pictureid);
            picture.setFilename(pictureName);
            picture.setAddress(pictureAddress);
            pictureService.save(picture);

            //保存Sort信息
            String sortId = idWorker.nextId();
            sort.setPictureUid(pictureid);
            sort.setId(sortId);
            baseMapper.insert(sort);
        }
    }

    @Override
    public R PageQuerySort(Long page, Long limit, QuerySortVo querySortVo) {
        QueryWrapper<Sort> queryWrapper = new QueryWrapper<>();
        Page<Sort> sortPage = new Page<>(page,limit);
        String title = querySortVo.getTitle();

        if (!StringUtils.isEmpty(title)){
            queryWrapper.like("title",title);
        }

        IPage<Sort> sortIPage = baseMapper.selectPage(sortPage, queryWrapper);
        Long total = sortIPage.getTotal();
        List<Sort> records = sortIPage.getRecords();

        List<Sort> collect = records.stream().map(record->{
            return editSortInfo(record);
        }).collect(Collectors.toList());

        return R.ok().data("total",total).data("list",collect);
    }

    @Override
    public Sort getSortById(String id) {
        Sort sort = baseMapper.selectById(id);
        Sort editSortInfo = editSortInfo(sort);
        return editSortInfo;
    }

    @Override
    public void updateSort(Sort sort) {
        String pictureUid = sort.getPictureUid();
        Picture picture = pictureService.getById(pictureUid);
        String filename = picture.getFilename();
        String pictureName = sort.getPictureName();
        if (filename.equals(pictureName)){
            baseMapper.updateById(sort);
        }else{
            //更新图片信息
            aliyunClient.deleteOss(filename);
            picture.setFilename(pictureName);
            picture.setAddress(sort.getPictureAddress());
            pictureService.updateById(picture);
            baseMapper.updateById(sort);
        }
    }

    @Override
    public void deleteSort(String id) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blog_sort_id",id);
        int count = blogService.count(queryWrapper);
        if (count>0){
            throw  new myException(20001,"该分类下有Blog，无法删除");
        }else {
            //todo 删除图片信息

            // 删除分类信息
            baseMapper.deleteById(id);
        }

    }

    private Sort editSortInfo(Sort record) {
        String pictureUid = record.getPictureUid();
        Picture picture = pictureService.getById(pictureUid);

        record.setPictureName(picture.getFilename());
        record.setPictureAddress(picture.getAddress());

        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blog_sort_id",record.getId());
        int count = blogService.count(queryWrapper);
        record.setBlogNum(count);

        return record;
    }
}
