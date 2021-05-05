package com.markerhub.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.demo.common.Result;
import com.markerhub.demo.entity.Class;
import com.markerhub.demo.entity.Live;
import com.markerhub.demo.service.ClassService;
import com.markerhub.demo.service.LiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhang564-code
 * @since 2021-05-01
 */
@RestController
public class ClassController {

    @Autowired
    ClassService classService;


    @PostMapping("/classroom")
    public Result list() {

        return Result.succ(classService.list());
    }

}
