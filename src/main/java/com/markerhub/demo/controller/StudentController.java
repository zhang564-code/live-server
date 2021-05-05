package com.markerhub.demo.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.demo.common.Result;
import com.markerhub.demo.common.dto.StudentDto;
import com.markerhub.demo.entity.Student;
import com.markerhub.demo.service.StudentService;
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
public class StudentController {

    @Autowired
    StudentService studentService;
    @Autowired
    JwtUtils jwtUtils;
    private BaseMapper<Student> baseMapper;

    @PostMapping("/student/login")
    public Result login(@Validated @RequestBody StudentDto studentDto, HttpServletResponse response) {


        Student student = studentService.getOne(new QueryWrapper<Student>().eq("student_name", studentDto.getStudent_name()));
        Assert.notNull(student ,"用户不存在");
        System.out.println(student.getStudentName());
        if(!student.getPassword().equals(md5(studentDto.getPassword()))) {
            return Result.fail("密码不正确");
        }
        String jwt = jwtUtils.generateStudentToken(student.getId());
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");
        return Result.succ(MapUtil.builder()
                .put("id", student.getId())
                .put("student_name", student.getStudentName())
                .put("avatar", student.getAvatar())
                .put("email", student.getEmail())
                .put("class_name", student.getClassName())
                .map());
    }

    @RequiresAuthentication
    @GetMapping("/student/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }

    @PostMapping("/student/register")
    @ResponseBody
    public Result Register(@RequestBody Student student){


        String student_name = student.getStudentName();
        //System.out.println(student_name);
        student_name = HtmlUtils.htmlEscape(student_name);

        String password = student.getPassword();

        //校验参数
        if (StringUtils.isEmpty(student_name)
                || StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException();
        }

        //是否被注册
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_name", student_name);
        Integer count = studentService.count(queryWrapper);
        if(count>0) {
            return Result.fail(400,"用户名已经存在",null);
        }

        student.setStudentName(student_name);
        student.setAvatar("https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg");
        student.setEmail("564283743@qq.com");
        student.setPassword(md5(password));
        student.setStatus(1);
        String className = student.getClassName();
        student.setClassName(className);
        student.setCreated(new Date());
        System.out.println(student);
        studentService.save(student);
        return Result.succ(200,"注册成功",null);
    }


    /**
     * 功能描述：测试Post请求
     * @return
     */
    @PostMapping("/student")
    public Result addStudent(@RequestBody Student student){
        String studentName = student.getStudentName();
        studentName = HtmlUtils.htmlEscape(studentName);
        student.setStudentName(studentName);
        String p = student.getPassword();

        student.setPassword(md5(p));
        //校验参数
        if (StringUtils.isEmpty(studentName)
                || StringUtils.isEmpty(p)) {
            throw new IllegalArgumentException();
        }

        //是否被注册
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_name", studentName);
        Integer count = studentService.count(queryWrapper);
        if(count>0) {
            return Result.fail(400,"用户名已经存在",null);
        }
        student.getAvatar();
        student.getEmail();
        student.getClassName();
        student.setStatus(1);
        student.setCreated(new Date());
        student.setLastLogin(new Date());

        studentService.save(student);
        return Result.succ(student);
    }


    @GetMapping("/student/{id}")
    public Result selectById(@PathVariable("id")Long id) {
         Student student = studentService.getById(id);
        return Result.succ(student);
    }

    @PutMapping("/student/{id}")
    public Result update(@PathVariable("id")Long id,@RequestBody Student student) {
        student.setId(id);
        student.getStudentName();
        student.getEmail();
        student.getAvatar();
        student.getClassName();
        studentService.updateById(student);

        return Result.succ(student);
    }

    @DeleteMapping("/student/{id}")
    public Result deleteById(@PathVariable("id")Long id) {

        studentService.removeById(id);

        return Result.succ(null);
    }

    @GetMapping("/students")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage,
                       @RequestParam(defaultValue = "3") Integer currentSize) {

        Page page = new Page(currentPage, currentSize);
        IPage pageData = studentService.page(page, new QueryWrapper<Student>().orderByAsc("created"));

        return Result.succ(pageData);
    }

    @GetMapping("/studentsearch")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage,
                       @RequestParam(defaultValue = "3") Integer currentSize,
                       @RequestParam String query) {

        Page page = new Page(currentPage, currentSize);
        IPage pageData = studentService.page(page, new QueryWrapper<Student>().like("student_name",query));

        return Result.succ(pageData);
    }

}
