package com.markerhub.demo.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.demo.common.Result;
import com.markerhub.demo.common.dto.TeacherDto;
import com.markerhub.demo.entity.Teacher;
import com.markerhub.demo.entity.Teacher;
import com.markerhub.demo.service.TeacherService;
import com.markerhub.demo.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static cn.hutool.crypto.SecureUtil.md5;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhang564-code
 * @since 2021-05-01
 */
@RestController
public class TeacherController {
    @Autowired
    TeacherService teacherService;
    @Autowired
    JwtUtils jwtUtils;
    private BaseMapper<Teacher> baseMapper;

    @PostMapping("/teacher/login")
    public Result login(@Validated @RequestBody TeacherDto teacherDto, HttpServletResponse response) {

        Teacher teacher = teacherService.getOne(new QueryWrapper<Teacher>().eq("teacher_name", teacherDto.getTeacher_name()));
        Assert.notNull(teacher ,"用户不存在");

        if(!teacher.getPassword().equals(md5(teacherDto.getPassword()))) {
            return Result.fail("密码不正确");
        }
        String jwt = jwtUtils.generateTeacherToken(teacher.getId());
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");
        return Result.succ(MapUtil.builder()
                .put("id", teacher.getId())
                .put("teacher_name", teacher.getTeacherName())
                .put("avatar", teacher.getAvatar())
                .put("email", teacher.getEmail())
                .map());
    }

    @RequiresAuthentication
    @GetMapping("/teacher/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }

    @PostMapping("/teacher/register")
    @ResponseBody
    public Result Register(@RequestBody Teacher teacher){


        String teacher_name = teacher.getTeacherName();
        System.out.println(teacher_name);
        teacher_name = HtmlUtils.htmlEscape(teacher_name);
        teacher.setTeacherName(teacher_name);
        String password = teacher.getPassword();

        //校验参数
        if (StringUtils.isEmpty(teacher_name)
                || StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException();
        }

        //是否被注册
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_name", teacher_name);
        Integer count = teacherService.count(queryWrapper);
        if(count>0) {
            return Result.fail(400,"用户名已经存在",null);
        }

        teacher.setTeacherName(teacher_name);
        teacher.setAvatar("https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg");
        teacher.setEmail("564283743@qq.com");
        teacher.setPassword(md5(password));
        teacher.setStatus(2);
        teacher.setClassName("软件四班");
        teacher.setCreated(new Date());
        System.out.println(teacher);
        teacherService.save(teacher);
        return Result.succ(200,"注册成功",null);
    }

    /**
     * 功能描述：测试Post请求
     * @return
     */
    @PostMapping("/teacher")
    public Result addTeacher(@RequestBody Teacher teacher){
        String teacherName = teacher.getTeacherName();
        teacherName = HtmlUtils.htmlEscape(teacherName);
        teacher.setTeacherName(teacherName);
        String p = teacher.getPassword();

        teacher.setPassword(md5(p));
        //校验参数
        if (StringUtils.isEmpty(teacherName)
                || StringUtils.isEmpty(p)) {
            throw new IllegalArgumentException();
        }

        //是否被注册
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_name", teacherName);
        Integer count = teacherService.count(queryWrapper);
        if(count>0) {
            return Result.fail(400,"用户名已经存在",null);
        }
        teacher.getAvatar();
        teacher.getEmail();
        teacher.getClassName();
        teacher.setStatus(2);
        teacher.setCreated(new Date());
        teacher.setLastLogin(new Date());

        teacherService.save(teacher);
        return Result.succ(teacher);
    }


    @GetMapping("/teacher/{id}")
    public Result selectById(@PathVariable("id")Long id) {
        Teacher teacher = teacherService.getById(id);
        return Result.succ(teacher);
    }

    @PutMapping("/teacher/{id}")
    public Result update(@PathVariable("id")Long id,@RequestBody Teacher teacher) {
        teacher.setId(id);
        teacher.getTeacherName();
        teacher.getEmail();
        teacher.getAvatar();
        teacher.getClassName();
        teacherService.updateById(teacher);

        return Result.succ(teacher);
    }

    @DeleteMapping("/teacher/{id}")
    public Result deleteById(@PathVariable("id")Long id) {

        teacherService.removeById(id);

        return Result.succ(null);
    }

    @GetMapping("/teachers")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage,
                       @RequestParam(defaultValue = "3") Integer currentSize) {

        Page page = new Page(currentPage, currentSize);
        IPage pageData = teacherService.page(page, new QueryWrapper<Teacher>().orderByAsc("created"));

        return Result.succ(pageData);
    }

    @GetMapping("/teachersearch")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage,
                       @RequestParam(defaultValue = "3") Integer currentSize,
                       @RequestParam String query) {

        Page page = new Page(currentPage, currentSize);
        IPage pageData = teacherService.page(page, new QueryWrapper<Teacher>().like("teacher_name",query));

        return Result.succ(pageData);
    }

}
