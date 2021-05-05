package com.markerhub.demo.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.demo.common.Result;
import com.markerhub.demo.entity.Menu;
import com.markerhub.demo.service.MenuService;
import com.markerhub.demo.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

import static cn.hutool.crypto.SecureUtil.md5;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhang564-code
 * @since 2021-05-01
 */
@RestController
public class MenuController {

    @Autowired
    MenuService menuService;

    @GetMapping("/menu")
    public Result list() {
        List<Menu> menus = menuService.list();
        return Result.succ(menus);
    }

    @GetMapping("/menus")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage,
                       @RequestParam(defaultValue = "3") Integer currentSize) {

        Page page = new Page(currentPage, currentSize);
        IPage pageData = menuService.page(page, new QueryWrapper<Menu>().orderByAsc("created"));

        return Result.succ(pageData);
    }

    @GetMapping("/menusearch")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage,
                       @RequestParam(defaultValue = "3") Integer currentSize,
                       @RequestParam String query) {

        Page page = new Page(currentPage, currentSize);
        IPage pageData = menuService.page(page, new QueryWrapper<Menu>().like("menu_name",query));

        return Result.succ(pageData);
    }

    @GetMapping("/menu/menustream/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        Menu menu = menuService.getById(id);
        Assert.notNull(menu, "该菜单已被删除");

        return Result.succ(menu);
    }

    @RequiresAuthentication
    @PostMapping("/menu/edit")
    public Result edit(@Validated @RequestBody Menu menu) {

//        Assert.isTrue(false, "公开版不能任意编辑！");

        Menu temp = null;
        if(menu.getId() != null) {
            temp = menuService.getById(menu.getId());
            // 只能编辑自己的菜单
            System.out.println(ShiroUtil.getProfile().getId());
            Assert.isTrue(temp.getId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");

        } else {

            temp = new Menu();
            temp.setId(ShiroUtil.getProfile().getId());
            temp.setCreated(new Date());
        }

        BeanUtil.copyProperties(menu, temp, "id", "userId", "date");
        menuService.saveOrUpdate(temp);

        return Result.succ(null);
    }

    @GetMapping("/menu/{id}")
    public Result selectById(@PathVariable("id")Long id) {
        Menu menu = menuService.getById(id);
        return Result.succ(menu);
    }


    @PutMapping("/menu/{id}")
    public Result update(@PathVariable("id")Long id,@RequestBody Menu menu) {
        menu.setId(id);
        menu.setCreated(new Date());
        menu.setParentId(0);
        menu.getMenuName();
        menu.getMenuUrl();
        menuService.updateById(menu);

        return Result.succ(menu);
    }

    /**
     * 功能描述：测试Post请求
     * @return
     */
    @PostMapping("/menu")
    public Result addMenu(@RequestBody Menu menu){
        String menuName = menu.getMenuName();
        menuName = HtmlUtils.htmlEscape(menuName);
        menu.setMenuName(menuName);
        //校验参数
        if (StringUtils.isEmpty(menuName)) {
            throw new IllegalArgumentException();
        }

        //是否存在
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("menu_name", menuName);
        Integer count = menuService.count(queryWrapper);
        if(count>0) {
            return Result.fail(400,"菜单名称已经存在",null);
        }
        menu.getMenuName();
        menu.getMenuUrl();
        menu.setParentId(0);
        menu.setCreated(new Date());

        menuService.save(menu);
        return Result.succ(menu);
    }

    @DeleteMapping("/menu/{id}")
    public Result deleteById(@PathVariable("id")Long id) {

        menuService.removeById(id);

        return Result.succ(null);
    }


}
