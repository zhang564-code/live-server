package com.markerhub.demo.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class TeacherDto implements Serializable {

    @NotBlank(message = "教师姓名不能为空")
    private String teacher_name;

    @NotBlank(message = "密码不能为空")
    private String password;
}
