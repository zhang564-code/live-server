package com.markerhub.demo.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.markerhub.demo.entity.Teacher;
import com.markerhub.demo.service.TeacherService;
import com.markerhub.demo.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TeacherRealm extends AuthorizingRealm {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    TeacherService teacherService;
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwt = (JwtToken) token;
        log.info("jwt----------------->{}", jwt);
        String teacherId = jwtUtils.getClaimByToken((String) jwt.getPrincipal()).getSubject();
        Teacher teacher = teacherService.getById(Long.parseLong(teacherId));
        if(teacher == null) {
            throw new UnknownAccountException("账户不存在！");
        }
        if(teacher.getStatus() == -1) {
            throw new LockedAccountException("账户已被锁定！");
        }
        TeacherProfile profile = new TeacherProfile();
        BeanUtil.copyProperties(teacher, profile);
        log.info("profile----------------->{}", profile.toString());
        return new SimpleAuthenticationInfo(profile, jwt.getCredentials(), getName());
    }
}

