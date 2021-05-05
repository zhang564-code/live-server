package com.markerhub.demo.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.markerhub.demo.common.Result;
import com.markerhub.demo.common.dto.LoginDto;
import com.markerhub.demo.entity.User;
import com.markerhub.demo.service.UserService;
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

@RestController
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;
    private BaseMapper<User> baseMapper;

    @PostMapping("/admin/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {

        User user = userService.getOne(new QueryWrapper<User>().eq("username",loginDto.getUsername()));
        Assert.notNull(user ,"用户不存在");

        if(!user.getPassword().equals(md5(loginDto.getPassword()))) {
            return Result.fail("密码不正确");
        }
        String jwt = jwtUtils.generateToken(user.getId());
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");
        return Result.succ(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("avatar", user.getAvatar())
                .put("email", user.getEmail())
                .map());
    }

    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }

    @PostMapping("/admin/register")
    @ResponseBody
    public Result Register(@RequestBody User user){

        
        String username = user.getUsername();
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);
        String password = user.getPassword();

        //校验参数
        if (StringUtils.isEmpty(username)
                || StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException();
        }

        //是否被注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Integer count = userService.count(queryWrapper);
        if(count>0) {
            return Result.fail(400,"用户名已经存在",null);
        }

        user.setUsername(username);
        user.setAvatar("https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg");
        user.setEmail("564283743@qq.com");
        user.setPassword(md5(password));
        user.setStatus(0);
        user.setCreated(new Date());
        System.out.println(user);
        userService.save(user);
        return Result.succ(200,"注册成功",null);
    }

}
