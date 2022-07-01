package com.krest.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krest.blog.client.AliyunClient;
import com.krest.blog.entity.Author;
import com.krest.blog.entity.Blog;
import com.krest.blog.entity.Picture;
import com.krest.blog.entity.vo.QueryAuthorVo;
import com.krest.blog.mapper.AuthorMapper;
import com.krest.blog.service.AuthorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.blog.service.BlogService;
import com.krest.blog.service.PictureService;
import com.krest.utils.myexception.myException;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 作者信息表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
@Service
public class AuthorServiceImpl extends ServiceImpl<AuthorMapper, Author> implements AuthorService {

    public static IdWorker idWorker = new IdWorker();

    @Autowired
    private PictureService pictureService;

    @Autowired
    private AliyunClient aliyunClient;

    @Autowired
    private BlogService blogService;

    @Override
    public Author getAuthorById(String id) {

        Author author = baseMapper.selectById(id);
        String pictureUid = author.getPictureUid();
        Picture picture = pictureService.getById(pictureUid);
        String pictureFilename = picture.getFilename();
        String pictureAddress = picture.getAddress();
        author.setPictureName(pictureFilename);
        author.setPictureAddress(pictureAddress);

        return author;
    }

    @Override
    public void saveAuthor(Author author) {
        String pictureId = idWorker.nextId();
        String authorId = idWorker.nextId();
        author.setId(authorId);
        author.setPictureUid(pictureId);

        baseMapper.insert(author);

        Picture picture = new Picture();
        picture.setId(pictureId);
        picture.setFilename(author.getPictureName());
        picture.setAddress(author.getPictureAddress());
        pictureService.save(picture);
    }

    @Override
    public R PageQueryAuthor(Long page, Long limit, QueryAuthorVo queryAuthorVo) {
        QueryWrapper<Author> queryWrapper = new QueryWrapper<>();
        Page<Author> pageAuthor = new Page<>(page,limit);
        String name = queryAuthorVo.getName();
        Boolean sex = queryAuthorVo.getSex();


        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(sex)){
            queryWrapper.eq("sex",sex);
        }

        IPage<Author> authorIPage = baseMapper.selectPage(pageAuthor, queryWrapper);
        Long total = authorIPage.getTotal();
        List<Author> records = authorIPage.getRecords();

        List<Author> collect = records.stream().map(record -> {
            return editAuthorInfo(record);
        }).collect(Collectors.toList());
        return R.ok().data("total",total).data("list",collect);
    }

    @Override
    public void updateAuthor(Author author) {
        String pictureUid = author.getPictureUid();

        //判断图片是否有更新
        Picture picture = pictureService.getById(pictureUid);
        String filename = picture.getFilename();

        if(author.getPictureName().equals(filename)){
            baseMapper.updateById(author);
        }else{
            aliyunClient.deleteOss(filename);
            picture.setFilename(author.getPictureName());
            picture.setAddress(author.getPictureAddress());
            pictureService.updateById(picture);
            baseMapper.updateById(author);
        }
    }

    @Override
    public void deleteAuthor(String id) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id",id);
        int count = blogService.count(queryWrapper);
        if (count>0){
            throw new myException(20001,"该作者有发表的文章，无法删除");
        }else{
            //因为是逻辑删除，所以并未将图片进行删除，后期会进行删除实际使用中，会直接进行删除
            baseMapper.deleteById(id);
        }
    }

    @Override
    public List<Author> webAuthorList() {
        List<Author> authors = baseMapper.selectList(null);
        List<Author> collect = authors.stream().map(author -> {
            return editAuthorInfo(author);
        }).collect(Collectors.toList());

        Collections.sort(collect, new Comparator<Author>() {
            @Override
            public int compare(Author o1, Author o2) {
                return o2.getPublishBlogNum().compareTo(o1.getPublishBlogNum());
            }
        });
        return collect;
    }

    private Author editAuthorInfo(Author record) {

        // 添加图片信息
        String pictureUid = record.getPictureUid();
        Picture picture = pictureService.getById(pictureUid);
        String pictureAddress = picture.getAddress();
        String filename = picture.getFilename();
        record.setPictureAddress(pictureAddress);
        record.setPictureName(filename);

        // 添加发表文章数量信息
        String id = record.getId();
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<Blog>();
        queryWrapper.eq("author_id",id);
        int count = blogService.count(queryWrapper);
        record.setPublishBlogNum(count);

        return record;
    }
}
