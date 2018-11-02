package com.jww.base.am.server.controller;


import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.entity.SysMenuEntity;
import com.jww.base.am.model.entity.SysTreeEntity;
import com.jww.base.am.server.annotation.SysLogOpt;
import com.jww.base.am.service.SysMenuService;
import com.jww.common.core.constant.enums.LogOptEnum;
import com.jww.common.web.BaseController;
import com.jww.common.web.model.dto.ResultDTO;
import com.jww.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
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
@Api(value = "菜单管理", description = "菜单管理")
public class SysMenuController extends BaseController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 查询所有菜单
     *
     * @return ResultDTO
     * @author wanyong
     * @date 2017-12-02 00:24
     */
    @ApiOperation(value = "查询菜单列表", notes = "查询全部菜单列表")
    @PostMapping("/queryList")
    // @RequiresAuthentication
    public ResultDTO queryList() {
        return ResultUtil.ok(sysMenuService.queryList());
    }

    /**
     * 分页查询菜单列表
     *
     * @param pageModel 分页实体
     * @return ResultDTO
     * @author shadj
     * @date 2017/12/18 21:34
     */
    @ApiOperation(value = "分页查询菜单列表", notes = "根据分页参数查询菜单列表")
    @PostMapping("/queryListPage")
    // @RequiresPermissions("sys:menu:read")
    public ResultDTO queryListPage(@RequestBody IPage page) {
        return ResultUtil.ok(sysMenuService.queryListPage(page));
    }

    @ApiOperation(value = "查询所有父级菜单列表", notes = "查询存在子菜单的菜单列表")
    @PostMapping("/queryParentMenu")
    // @RequiresPermissions("sys:menu:read")
    public ResultDTO queryParentMenu() {
        return ResultUtil.ok(sysMenuService.queryParentMenu());
    }

    /**
     * 查询用户权限菜单
     *
     * @param userId 用户ID
     * @return ResultDTO
     * @author shadj
     * @date 2017/12/18 21:36
     */
    @ApiOperation(value = "查询用户权限菜单", notes = "根据用户ID查询用户权限菜单")
    @GetMapping("/tree/{userId}")
    // @RequiresAuthentication
    public ResultDTO queryMenuTreeByUserId(@PathVariable(value = "userId") Long userId) {
        return ResultUtil.ok(sysMenuService.queryMenuTreeByUserId(userId));
    }

    /**
     * 根据ID删除菜单
     *
     * @param id 菜单ID
     * @return ResultDTO
     * @author shadj
     * @date 2017/12/18 21:51
     */
    @ApiOperation(value = "删除菜单", notes = "根据菜单ID删除菜单")
    @DeleteMapping("/delete")
    // @RequiresPermissions("sys:menu:delete")
    @SysLogOpt(module = "菜单管理", value = "菜单删除", operationType = LogOptEnum.DELETE)
    public ResultDTO delete(@RequestBody Long id) {
        return ResultUtil.ok(sysMenuService.delete(id));
    }

    /**
     * 批量删除菜单
     *
     * @param ids 菜单ID集合
     * @return ResultDTO
     * @author shadj
     * @date 2017/12/18 21:52
     */
    @ApiOperation(value = "批量删除菜单", notes = "根据主键ID集合批量删除菜单")
    @PostMapping("/deleteBatchIds")
    // @RequiresPermissions("sys:menu:delete")
    @SysLogOpt(module = "菜单管理", value = "菜单批量删除", operationType = LogOptEnum.DELETE)
    public ResultDTO deleteBatchIds(@RequestBody Long[] ids) {
        Assert.notNull(ids);
        return ResultUtil.ok(sysMenuService.deleteBatch(ids));
    }

    /**
     * 查询菜单
     *
     * @param id 菜单ID
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-03 14:10
     */
    @ApiOperation(value = "查询菜单", notes = "根据主键ID查询菜单")
    @GetMapping("/query/{id}")
    // @RequiresPermissions("sys:menu:read")
    public ResultDTO query(@PathVariable(value = "id") Long id) {
        Assert.notNull(id);
        SysMenuEntity sysMenuEntity = sysMenuService.getById(id);
        return ResultUtil.ok(sysMenuEntity);
    }

    /**
     * 根据ID修改菜单
     *
     * @param sysMenuEntity 菜单实体
     * @return ResultDTO
     * @author shadj
     * @date 2017/12/18 21:54
     */
    @ApiOperation(value = "修改菜单", notes = "根据主键ID修改菜单")
    @PostMapping("/modify")
    // @RequiresPermissions("sys:menu:update")
    @SysLogOpt(module = "菜单管理", value = "菜单修改", operationType = LogOptEnum.MODIFY)
    public ResultDTO modify(@RequestBody SysMenuEntity sysMenuEntity) {
        sysMenuEntity.setUpdateBy(super.getCurrentUserId());
        sysMenuEntity.setUpdateTime(new Date());
        sysMenuService.modifyById(sysMenuEntity);
        return ResultUtil.ok();
    }

    /**
     * 新增菜单
     *
     * @param sysMenuEntity 菜单实体
     * @return ResultDTO
     * @author shadj
     * @date 2017/12/18 21:54
     */
    @ApiOperation(value = "新增菜单", notes = "根据菜单实体新增菜单")
    @PostMapping("/add")
    // @RequiresPermissions("sys:menu:add")
    @SysLogOpt(module = "菜单管理", value = "菜单新增", operationType = LogOptEnum.ADD)
    public ResultDTO add(@Valid @RequestBody SysMenuEntity sysMenuEntity) {
        if (sysMenuEntity != null) {
            Date now = new Date();
            sysMenuEntity.setCreateTime(now);
            sysMenuEntity.setCreateBy(super.getCurrentUserId());
            sysMenuEntity.setUpdateBy(super.getCurrentUserId());
            sysMenuEntity.setUpdateTime(now);
        }
        sysMenuService.add(sysMenuEntity);
        return ResultUtil.ok();
    }

    /**
     * 根据角色ID查询角色对应权限功能树
     *
     * @param roleId 角色ID
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-03 19:20
     */
    @ApiOperation(value = "查询权限功能树", notes = "根据角色ID查询角色对应权限功能树")
    @PostMapping("/roleFuncTree")
    // @RequiresPermissions("sys:menu:read")
    public ResultDTO queryFuncMenuTree(@RequestBody Long roleId) {
        List<SysTreeEntity> treeModelList = sysMenuService.queryFuncMenuTree(roleId);
        return ResultUtil.ok(treeModelList);
    }

    /**
     * 查询所有权限功能树
     *
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-03 19:22
     */
    @ApiOperation(value = "查询所有权限功能树", notes = "查询所有权限功能树")
    @PostMapping("/funcTree")
    // @RequiresPermissions("sys:menu:read")
    public ResultDTO queryFuncMenuTree() {
        List<SysTreeEntity> treeModelList = sysMenuService.queryFuncMenuTree(null);
        return ResultUtil.ok(treeModelList);
    }

    /**
     * 根据菜单类型和菜单ID查询菜单树
     *
     * @param menuType 菜单类型
     * @param menuId   菜单ID
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-03 19:23
     */
    @ApiOperation(value = "查询菜单树", notes = "根据菜单类型和菜单ID查询菜单树")
    @GetMapping("/queryTree/{menuType}/{menuId}")
    // @RequiresPermissions("sys:menu:read")
    public ResultDTO queryTree(@PathVariable(required = false, value = "menuType") Integer menuType, @PathVariable(value = "menuId") Long menuId) {
        List<SysTreeEntity> list = sysMenuService.queryTree(menuId, menuType);
        return ResultUtil.ok(list);
    }

    /**
     * 根据菜单类型查询菜单树
     *
     * @param menuType 菜单类型
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-03 19:25
     */
    @ApiOperation(value = "查询菜单树", notes = "根据菜单类型查询菜单树")
    @GetMapping("/queryTree/{menuType}")
    // @RequiresPermissions("sys:menu:read")
    public ResultDTO queryTree(@PathVariable(required = false, value = "menuType") Integer menuType) {
        List<SysTreeEntity> list = sysMenuService.queryTree(null, menuType);
        return ResultUtil.ok(list);
    }
}

