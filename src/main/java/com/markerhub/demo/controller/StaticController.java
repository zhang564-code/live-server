package com.markerhub.demo.controller;


import com.markerhub.demo.common.Result;
import com.markerhub.demo.entity.Static;
import com.markerhub.demo.service.StaticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhang564-code
 * @since 2021-05-04
 */
@RestController
public class StaticController {

    @Autowired
    private StaticService staticService;
    @RequestMapping("/report/data/1")
    public Result findAll(){

        return Result.succ(staticService.list());
    }

}
