package com.markerhub.demo.shiro;

import lombok.Data;

import java.io.Serializable;

@Data
public class StudentProfile implements Serializable {
    private Long id;
    private String studentName;
    private String avatar;
}

