package com.markerhub.demo.util;

import com.markerhub.demo.shiro.AccountProfile;
import com.markerhub.demo.shiro.StudentProfile;
import com.markerhub.demo.shiro.TeacherProfile;
import org.apache.shiro.SecurityUtils;

public class ShiroUtil {

    public static AccountProfile getProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }

    public static StudentProfile getStudentProfile() {
        return (StudentProfile) SecurityUtils.getSubject().getPrincipal();
    }

    public static TeacherProfile getTeacherProfile() {
        return (TeacherProfile) SecurityUtils.getSubject().getPrincipal();
    }

}
