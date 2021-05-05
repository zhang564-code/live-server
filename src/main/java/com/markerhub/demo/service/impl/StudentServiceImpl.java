package com.markerhub.demo.service.impl;

import com.markerhub.demo.entity.Student;
import com.markerhub.demo.mapper.StudentMapper;
import com.markerhub.demo.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhang564-code
 * @since 2021-05-01
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}
