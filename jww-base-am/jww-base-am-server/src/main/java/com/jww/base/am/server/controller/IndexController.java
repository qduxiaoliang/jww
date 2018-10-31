package com.jww.base.am.server.controller;

import com.jww.base.am.api.SysAuthorizeService;
import com.jww.base.am.api.SysMenuService;
import com.jww.common.web.BaseController;
import com.jww.common.web.model.ResultModel;
import com.jww.common.web.util.ResultUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页前端控制器
 *
 * @author shadj
 * @date 2017/12/31 21:13
 */
@RestController
@RequestMapping("/index")
public class IndexController extends BaseController {

    @Autowired
    private SysAuthorizeService sysAuthorizeService;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 获取当前用户的权限集合
     *
     * @return ResultModel
     * @author shadj
     * @date 2017/12/31 21:10
     */
    @GetMapping("/permissions")
    @RequiresAuthentication
    public ResultModel queryMyPermissions() {
        return ResultUtil.ok(sysAuthorizeService.queryPermissionsByUserId(super.getCurrentUserId()));
    }

    /**
     * 获取当前用户的菜单树
     *
     * @return ResultModel
     * @author wanyong
     * @date 2018-01-07 14:17
     */
    @GetMapping("/menuTree")
    @RequiresAuthentication
    public ResultModel queryMyMenuTree() {
        return ResultUtil.ok(sysMenuService.queryMenuTreeByUserId(super.getCurrentUserId()));
    }
}
