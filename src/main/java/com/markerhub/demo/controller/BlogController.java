package com.markerhub.demo.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.demo.common.Result;
import com.markerhub.demo.entity.Blog;
import com.markerhub.demo.entity.Live;
import com.markerhub.demo.service.BlogService;
import com.markerhub.demo.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2021-04-25
 */
@RestController
public class BlogController {

    @Autowired
    BlogService blogService;

    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {

        Page page = new Page(currentPage, 5);
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));

        return Result.succ(pageData);
    }

    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已被删除");

        return Result.succ(blog);
    }

    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {

//        Assert.isTrue(false, "公开版不能任意编辑！");

        Blog temp = null;
        if(blog.getId() != null) {
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            System.out.println(ShiroUtil.getProfile().getId());
            Assert.isTrue(temp.getId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");

        } else {

            temp = new Blog();
            temp.setId(ShiroUtil.getProfile().getId());
            temp.setCreated(new Date());
            temp.setStatus(0);
        }

        BeanUtil.copyProperties(blog, temp, "id", "userName", "created", "status");
        blogService.saveOrUpdate(temp);

        return Result.succ(null);
    }

    @RequiresAuthentication
    @PostMapping("/blog/student/edit")
    public Result studentEdit(@Validated @RequestBody Blog blog) {

//        Assert.isTrue(false, "公开版不能任意编辑！");

        Blog temp = null;
        if(blog.getId() != null) {
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            System.out.println(ShiroUtil.getStudentProfile().getId());
            Assert.isTrue(temp.getId().longValue() == ShiroUtil.getStudentProfile().getId().longValue(), "没有权限编辑");

        } else {

            temp = new Blog();
            temp.setUserName(ShiroUtil.getStudentProfile().getStudentName());
            temp.setCreated(new Date());
            temp.setStatus(1);
        }

        BeanUtil.copyProperties(blog, temp, "id", "userName", "created", "status");
        blogService.saveOrUpdate(temp);

        return Result.succ(null);
    }

    @RequiresAuthentication
    @PostMapping("/blog/teacher/edit")
    public Result teacherEdit(@Validated @RequestBody Blog blog) {

//        Assert.isTrue(false, "公开版不能任意编辑！");

        Blog temp = null;
        if(blog.getId() != null) {
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            System.out.println(ShiroUtil.getTeacherProfile().getId());
            Assert.isTrue(temp.getId().longValue() == ShiroUtil.getTeacherProfile().getId().longValue(), "没有权限编辑");

        } else {

            temp = new Blog();
            System.out.println(ShiroUtil.getTeacherProfile().getId());
            temp.setUserName(ShiroUtil.getTeacherProfile().getTeacherName());
            temp.setCreated(new Date());
            temp.setStatus(1);
        }

        BeanUtil.copyProperties(blog, temp, "id", "userName", "created", "status");
        blogService.saveOrUpdate(temp);

        return Result.succ(null);
    }

    @PutMapping("/blog/{id}")
    public Result update(@PathVariable("id")Long id,@RequestBody Blog blog) {
        blog.setId(id);
        blog.setCreated(new Date());
        blog.setStatus(1);
        blog.getTitle();
        blog.getDesription();
        blog.getContent();
        blog.getUserName();
        blogService.updateById(blog);

        return Result.succ(blog);
    }


    @DeleteMapping("/blog/{id}")
    public Result deleteById(@PathVariable("id")Long id) {

        blogService.removeById(id);

        return Result.succ(null);
    }

    @GetMapping("/blogsearch")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage,
                       @RequestParam(defaultValue = "3") Integer currentSize,
                       @RequestParam String query) {

        Page page = new Page(currentPage, currentSize);
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().like("title",query));

        return Result.succ(pageData);
    }

}
