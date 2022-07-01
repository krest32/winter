package com.krest.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krest.blog.client.AliyunClient;
import com.krest.blog.entity.*;
import com.krest.blog.entity.vo.BlogInfo;
import com.krest.blog.entity.vo.QueryBlogVo;
import com.krest.blog.mapper.BlogMapper;
import com.krest.blog.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 博客表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    public static IdWorker idWorker = new IdWorker();

    @Autowired
    private AuthorService authorService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private SortService sortService;

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRelationService tagRelationService;

    @Autowired
    private AliyunClient aliyunClient;

    @Override
    public R PageQueryBlog(Long page, Long limit, QueryBlogVo queryBlogVo) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        Page<Blog> blogPage = new Page<>(page,limit);

        String authorId = queryBlogVo.getAuthorId();
        String blogSortId = queryBlogVo.getBlogSortId();
        String title = queryBlogVo.getTitle();
        Integer status = queryBlogVo.getStatus();

        if (!StringUtils.isEmpty(authorId)){
            queryWrapper.eq("author_id",authorId);
        }
        if (!StringUtils.isEmpty(blogSortId)){
            queryWrapper.eq("blog_sort_id",blogSortId);
        }
        if (!StringUtils.isEmpty(title)){
            queryWrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(status)){
            queryWrapper.eq("status",status);
        }

        IPage<Blog> blogIPage = baseMapper.selectPage(blogPage, queryWrapper);
        Long total = blogIPage.getTotal();
        List<Blog> records = blogIPage.getRecords();

        List<Blog> collect = records.stream().map(record->{
            return editBlogInfo(record);
        }).collect(Collectors.toList());

        System.out.println("collect:"+collect);
        return R.ok().data("total",total).data("list",collect);
    }

    @Override
    public void addBlog(Blog blog) {
        //设置BlogId
        String blogId = idWorker.nextId();

        //保存图片信息
        String pictureAddress = blog.getPictureAddress();
        String pictureName = blog.getPictureName();
        String pictureId = idWorker.nextId();
        Picture picture = new Picture();
        picture.setAddress(pictureAddress);
        picture.setFilename(pictureName);
        picture.setId(pictureId);
        pictureService.save(picture);
        blog.setPictureUid(pictureId);

        //添加tagRelation信息
        List<String> tagIdList = blog.getTagIdList();
        for (String s : tagIdList) {
            TagRelation tagRelation = new TagRelation();
            tagRelation.setId(idWorker.nextId());
            tagRelation.setBlogId(blogId);
            tagRelation.setTagId(s);
            tagRelationService.save(tagRelation);
        }

        //保存blog
        blog.setId(blogId);
        baseMapper.insert(blog);
    }

    @Override
    public void changeStatus(String id) {
        Blog blog = baseMapper.selectById(id);
        Boolean status = blog.getStatus();
        if(status){
            blog.setStatus(false);
        }else{
            blog.setStatus(true);
        }
        baseMapper.updateById(blog);
    }

    @Override
    public void deleteBlog(String id) {
        Blog blog= editBlogInfo(baseMapper.selectById(id));

        //删除图片信息
        String pictureName = blog.getPictureName();
        aliyunClient.deleteOss(pictureName);
        String pictureUid = blog.getPictureUid();
        pictureService.removeById(pictureUid);

        //删除标签关系表内容
        String blogId = blog.getId();
        QueryWrapper<TagRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blog_id",blogId);
        List<TagRelation> list = tagRelationService.list(queryWrapper);
        List<String> tagRelationIdList = new ArrayList<>();
        for (TagRelation tagRelation : list) {
            String tagRelationId = tagRelation.getId();
            tagRelationIdList.add(tagRelationId);
        }
        tagRelationService.removeByIds(tagRelationIdList);

        //删除blog
        baseMapper.deleteById(id);
    }

    @Override
    public Blog getBlogById(String id) {
        Blog blog = editBlogInfo(baseMapper.selectById(id));
        return blog;
    }

    @Override
    public void updateBlog(Blog blog) {

        //校验图片信息
        String pictureUid = blog.getPictureUid();
        Picture picture = pictureService.getById(pictureUid);
        String filename = picture.getFilename();
        String pictureName = blog.getPictureName();
        if(!filename.equals(pictureName)){
            aliyunClient.deleteOss(filename);
            picture.setFilename(pictureName);
            picture.setAddress(blog.getPictureAddress());
            pictureService.updateById(picture);
        }

        //更新tagRelation信息
        //1.先删除
        String blogId = blog.getId();
        QueryWrapper<TagRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blog_id",blogId);
        List<TagRelation> list = tagRelationService.list(queryWrapper);
        List<String> tagRelationIdList = new ArrayList<>();
        for (TagRelation tagRelation : list) {
            String tagRelationId = tagRelation.getId();
            tagRelationIdList.add(tagRelationId);
        }
        tagRelationService.removeByIds(tagRelationIdList);
        //2.重新添加
        List<String> tagIdList = blog.getTagIdList();
        for (String s : tagIdList) {
            TagRelation tagRelation = new TagRelation();
            tagRelation.setId(idWorker.nextId());
            tagRelation.setBlogId(blogId);
            tagRelation.setTagId(s);
            tagRelationService.save(tagRelation);
        }

        //更新blog信息
        baseMapper.updateById(blog);
    }

    @Override
    public R getBlogByAuthor(String id, Long page,Long limit) {
        Page<Blog> blogPage =new Page<>(page,limit);
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id",id);
        IPage<Blog> blogIPage = baseMapper.selectPage(blogPage, queryWrapper);
        List<Blog> blogList = blogIPage.getRecords();
        long total = blogIPage.getTotal();
        List<Blog> collect = blogList.stream().map(blog -> {
            return editBlogInfo(blog);
        }).collect(Collectors.toList());

        return R.ok().data("total",total).data("blogList",collect);
    }

    @Override
    public List<Blog> getHotBlog() {

         int num = 8;
         List<Blog> finalList = new ArrayList<>();

        // 得到 Blog 数量不为 0 的分类
        List<Sort> sortList = sortService.list(null);
        List<Sort> sorts = sortList.stream().map(sort -> {
            return editSortInfo(sort);
        }).filter(sort -> {
            return sort.getBlogNum() > 0;
        }).collect(Collectors.toList());

        // 计算权重
        List<Sort> newSorts = GetWeightSort(sorts , num);

        System.out.println("newSorts:" + newSorts);

        // 得到需要的Blog集合
        for (Sort sort : newSorts) {
            System.out.println("sort:"+sort.getTitle()+":"+sort.getBlogNumWeight());
            Integer blogNumWeight = sort.getBlogNumWeight();
            QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("blog_sort_id",sort.getId());
            queryWrapper.orderByDesc("click_count");
            queryWrapper.last("limit "+blogNumWeight);
            List<Blog> blogList = baseMapper.selectList(queryWrapper);
            List<Blog> collect = blogList.stream().map(blog -> {
                return editBlogInfo(blog);
            }).collect(Collectors.toList());
            finalList.addAll(collect);
        }

        return finalList;
    }

    @Override
    public Blog WebGetBlogById(String id) {
        Blog blog = baseMapper.selectById(id);
        Long clickCount = blog.getClickCount();
        clickCount=clickCount+1;
        blog.setClickCount(clickCount);
        baseMapper.updateById(blog);
        Blog blogInfo = editBlogInfo(blog);
        return blogInfo;
    }

    private List<Sort> GetWeightSort(List<Sort> sorts,int num) {
        List<Sort> list = new ArrayList<>();

        int sum = 0 ;
        for (Sort sort : sorts) {
            sum=sort.getBlogNum()+sum;
        }

        for (Sort sort : sorts) {
            Double blogNum = new Double(sort.getBlogNum());
            int weight = (int) Math.round(num*(blogNum/sum));
            System.out.println("weight："+sort.getTitle()+":"+weight);
            sort.setBlogNumWeight(weight);
            list.add(sort);
        }
        return list;
    }


    private Sort editSortInfo(Sort record) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blog_sort_id",record.getId());
        int count = baseMapper.selectCount(queryWrapper);
        record.setBlogNum(count);

        return record;
    }

    private Blog editBlogInfo(Blog record) {
        // 添加Author信息
        String authorId = record.getAuthorId();
        Author authorById = authorService.getAuthorById(authorId);
        record.setAuthorName(authorById.getName());
        Picture authorPicture = pictureService.getById(authorById.getPictureUid());
        record.setAuthorAvatar(authorPicture.getAddress());
        record.setAuthorSummary(authorById.getSummary());

        // 添加图片信息
        String pictureUid = record.getPictureUid();
        Picture picture = pictureService.getById(pictureUid);
        record.setPictureName(picture.getFilename());
        record.setPictureAddress(picture.getAddress());

        // 添加分类信息
        String blogSortId = record.getBlogSortId();
        Sort sort = sortService.getById(blogSortId);
        record.setBlogSortTitle(sort.getTitle());

        // 添加tagIdList
        QueryWrapper<TagRelation> queryWrapper = new QueryWrapper<>();
        String recordId = record.getId();
        queryWrapper.eq("blog_id",recordId);
        List<TagRelation> list = tagRelationService.list(queryWrapper);
        List<String> tagIdList = new ArrayList<>();
        for (TagRelation tagRelation : list) {
            String tagId = tagRelation.getTagId();
            tagIdList.add(tagId);
        }
        record.setTagIdList(tagIdList);


        //添加所有的tag信息
        List<Tag> tagList = (List<Tag>) tagService.listByIds(record.getTagIdList());
        record.setTagList(tagList);

        return record;
    }
}
