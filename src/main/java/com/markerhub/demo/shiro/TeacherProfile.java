package com.markerhub.demo.shiro;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeacherProfile implements Serializable {
    private Long id;
    private String teacherName;
    private String avatar;
}

