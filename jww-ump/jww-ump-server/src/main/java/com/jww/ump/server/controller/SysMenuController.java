package com.jww.ump.server.controller;


import com.jww.common.web.BaseController;
import com.jww.common.web.model.ResultModel;
import com.jww.common.web.util.ResultUtil;
import com.jww.ump.model.UmpMenuModel;
import com.jww.ump.rpc.api.UmpMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 菜单 前端控制器
 * </p>
 *
 * @author wanyong
 * @since 2017-11-29
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController extends BaseController {

    @Autowired
    private UmpMenuService umpMenuService;

    /**
     * 查询所有菜单
     *
     * @param
     * @return
     * @author wanyong
     * @date 2017-12-02 00:24
     */
    @GetMapping("/list")
    public ResultModel<List<UmpMenuModel>> queryList() {
        return ResultUtil.ok(umpMenuService.queryList());
    }

    @GetMapping("/tree/{userId}")
    public ResultModel<List<UmpMenuModel>> queryMenuTreeByUserId(@PathVariable(value = "userId") Long userId) {
        return ResultUtil.ok(umpMenuService.queryMenuTreeByUserId(userId));
    }

}

