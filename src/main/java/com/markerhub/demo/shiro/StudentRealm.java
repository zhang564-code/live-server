package com.markerhub.demo.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.markerhub.demo.entity.Student;
import com.markerhub.demo.service.StudentService;
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
public class StudentRealm extends AuthorizingRealm {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    StudentService studentService;
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
        String studentId = jwtUtils.getClaimByToken((String) jwt.getPrincipal()).getSubject();
        Student student = studentService.getById(Long.parseLong(studentId));
        if(student == null) {
            throw new UnknownAccountException("账户不存在！");
        }
        if(student.getStatus() == -1) {
            throw new LockedAccountException("账户已被锁定！");
        }
        StudentProfile profile = new StudentProfile();
        BeanUtil.copyProperties(student, profile);
        log.info("profile----------------->{}", profile.toString());
        return new SimpleAuthenticationInfo(profile, jwt.getCredentials(), getName());
    }
}

