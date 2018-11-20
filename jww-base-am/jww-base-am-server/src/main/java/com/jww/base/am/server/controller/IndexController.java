package com.jww.base.am.server.controller;

import com.jww.base.am.service.SysAuthorizeService;
import com.jww.base.am.service.SysResourceService;
import com.jww.common.web.BaseController;
import com.jww.common.web.model.dto.Result;
import com.jww.common.web.util.ResultUtil;
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
    private SysResourceService sysResourceService;

    /**
     * 获取当前用户的权限集合
     *
     * @return Result
     * @author shadj
     * @date 2017/12/31 21:10
     */
    @GetMapping("/permissions")
    // @RequiresAuthentication
    public Result queryMyPermissions() {
        return ResultUtil.ok(sysAuthorizeService.listPermissionByUserId(super.getCurrentUserId()));
    }

    /**
     * 获取当前用户的菜单树
     *
     * @return Result
     * @author wanyong
     * @date 2018-01-07 14:17
     */
    @GetMapping("/menuTree")
    // @RequiresAuthentication
    public Result queryMyMenuTree() {
        return ResultUtil.ok(sysResourceService.listMenuTreeByUserId(super.getCurrentUserId()));
    }
}
