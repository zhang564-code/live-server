package com.markerhub.demo.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class StudentDto implements Serializable {

    @NotBlank(message = "学生姓名不能为空")
    private String student_name;

    @NotBlank(message = "密码不能为空")
    private String password;
}
