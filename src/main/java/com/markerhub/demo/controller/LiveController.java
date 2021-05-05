package com.markerhub.demo.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.demo.common.Result;
import com.markerhub.demo.entity.Blog;
import com.markerhub.demo.entity.Live;
import com.markerhub.demo.entity.Live;
import com.markerhub.demo.service.LiveService;
import com.markerhub.demo.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2021-04-27
 */
@RestController
public class LiveController {

    @Autowired
    LiveService liveService;

    @GetMapping("/lives")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage,
                       @RequestParam(defaultValue = "3") Integer currentSize) {

        Page page = new Page(currentPage, currentSize);
        IPage pageData = liveService.page(page, new QueryWrapper<Live>().orderByAsc("date"));

        return Result.succ(pageData);
    }

    @GetMapping("/livesearch")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage,
                       @RequestParam(defaultValue = "3") Integer currentSize,
                       @RequestParam String query) {

        Page page = new Page(currentPage, currentSize);
        IPage pageData = liveService.page(page, new QueryWrapper<Live>().like("livename",query));

        return Result.succ(pageData);
    }

    @GetMapping("/live/livestream/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        Live live = liveService.getById(id);
        Assert.notNull(live, "该直播已被删除");

        return Result.succ(live);
    }

    @RequiresAuthentication
    @PostMapping("/live/edit")
    public Result edit(@Validated @RequestBody Live live) {
        System.out.println(live.toString());
        Live temp = null;
        if(live.getId() != null) {
            temp = liveService.getById(live.getId());
            Assert.isTrue(temp.getUserName() == ShiroUtil.getTeacherProfile().getTeacherName(), "没有权限编辑");
        } else {
            temp = new Live();
            temp.setUserName(ShiroUtil.getTeacherProfile().getTeacherName());
            temp.setDate(new Date());
            temp.getLivename();
        }
        BeanUtil.copyProperties(live, temp, "id", "userName", "date");
        liveService.saveOrUpdate(temp);
        return Result.succ(200,"操作成功", null);
    }


    @PutMapping("/live/{id}")
    public Result update(@PathVariable("id")Long id,@RequestBody Live live) {
        live.setId(id);
        live.setDate(new Date());
        live.getUserName();
        live.getLivename();
        liveService.updateById(live);

        return Result.succ(live);
    }

    @GetMapping("/live/{id}")
    public Result selectById(@PathVariable("id")Long id) {
        Live live = liveService.getById(id);
        return Result.succ(live);
    }


    @DeleteMapping("/live/{id}")
    public Result deleteById(@PathVariable("id")Long id) {

        liveService.removeById(id);

        return Result.succ(null);
    }


}
