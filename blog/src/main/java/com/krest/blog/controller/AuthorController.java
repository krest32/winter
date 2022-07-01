package com.krest.blog.controller;


import com.krest.blog.entity.Author;
import com.krest.blog.entity.vo.BlogInfo;
import com.krest.blog.entity.vo.QueryAuthorVo;
import com.krest.blog.entity.vo.QueryBlogVo;
import com.krest.blog.service.AuthorService;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 作者信息表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-04
 */
@CrossOrigin
@Api(value = "作者管理",tags ="作者管理")
@RestController
@RequestMapping("/blog/author")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @ApiOperation(value = "根据Id查找Author信息")
    @GetMapping("getAuthorById/{id}")
    public R getAuthorById(@PathVariable String id){
        Author author = authorService.getAuthorById(id);
        return R.ok().data("author",author);
    }

    @ApiOperation(value = "添加新的Author信息")
    @PostMapping("addAuthor")
    public R addAuthor(@RequestBody Author author){
        authorService.saveAuthor(author);
        return R.ok();
    }

    @ApiOperation(value = "条件分页条件查询Blog")
    @PostMapping("PageQueryAuthor/{page}/{limit}")
    public R PageQueryAuthor(@PathVariable Long page,
                           @PathVariable Long limit,
                           @RequestBody(required = false) QueryAuthorVo queryAuthorVo){
        return authorService.PageQueryAuthor(page,limit,queryAuthorVo);
    }

    @ApiOperation(value = "修改Author信息")
    @PostMapping("updateAuthor")
    public R updateAuthor(@RequestBody Author author){
        authorService.updateAuthor(author);
        return R.ok();
    }

    @ApiOperation(value = "修改Author信息")
    @DeleteMapping("deleteAuthor/{id}")
    public R deleteAuthor(@PathVariable String id){
        authorService.deleteAuthor(id);
        return R.ok();
    }

    @ApiOperation(value = "获取Author下来列表")
    @GetMapping("listAllAuthor")
    public R listAllAuthor(){
        List<Author> list = authorService.list(null);
        return R.ok().data("authorList",list);
    }


    @ApiOperation(value = "Web: 获取首页作者列表")
    @GetMapping("webAuthorList")
    public R webAuthorList(){
        List<Author> authorList= authorService.webAuthorList();
        return R.ok().data("authorList",authorList);
    }


}

