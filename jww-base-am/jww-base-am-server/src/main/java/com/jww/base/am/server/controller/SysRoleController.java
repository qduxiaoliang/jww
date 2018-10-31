package com.jww.base.am.server.controller;

import com.jww.base.am.api.SysDeptService;
import com.jww.base.am.api.SysRoleService;
import com.jww.base.am.model.entity.SysDeptEntity;
import com.jww.base.am.model.entity.SysRoleEntity;
import com.jww.base.am.server.annotation.SysLogOpt;
import com.jww.common.core.Constants;
import com.jww.common.core.exception.BusinessException;
import com.jww.common.core.model.PageModel;
import com.jww.common.web.BaseController;
import com.jww.common.web.model.ResultModel;
import com.jww.common.web.util.ResultUtil;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.util.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 角色信息表 前端控制器
 * </p>
 *
 * @author wanyong
 * @since 2017-11-17
 */
@RestController
@RequestMapping("/role")
@Api(value = "角色管理", description = "角色管理")
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 根据角色ID查询角色
     *
     * @param roleId
     * @return ResultModel<SysRoleModel>
     * @author wanyong
     * @date 2017-12-05 13:35
     */
    @ApiOperation(value = "查询角色", notes = "根据角色主键ID查询角色")
    @ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "Long")
    @PostMapping("/query")
    @RequiresPermissions("sys:role:read")
    public ResultModel query(@RequestBody Long roleId) {
        Assert.notNull(roleId);
        SysRoleEntity sysRoleEntity = sysRoleService.queryById(roleId);
        SysDeptEntity sysDeptEntity = sysDeptService.queryById(sysRoleEntity.getDeptId());
        sysRoleEntity.setDeptName(sysDeptEntity.getDeptName());
        return ResultUtil.ok(sysRoleEntity);
    }

    /**
     * 分页查询角色列表
     *
     * @param pageModel 分页实体
     * @return ResultModel
     * @author wanyong
     * @date 2018-01-04 11:25
     */
    @ApiOperation(value = "分页查询角色列表", notes = "根据分页参数查询角色列表")
    @PostMapping("/listPage")
    @RequiresPermissions("sys:role:read")
    public ResultModel queryListPage(@RequestBody PageModel pageModel) {
        return ResultUtil.ok(sysRoleService.queryListPage(pageModel));
    }

    /**
     * 新增角色
     *
     * @param sysRoleEntity 角色实体
     * @return ResultModel
     * @author wanyong
     * @date 2018-01-04 11:26
     */
    @ApiOperation(value = "新增角色", notes = "根据角色实体新增角色")
    @PostMapping("/add")
    @RequiresPermissions("sys:role:add")
    @SysLogOpt(module = "角色管理", value = "角色新增", operationType = Constants.LogOptEnum.ADD)
    public ResultModel add(@Valid @RequestBody SysRoleEntity sysRoleEntity) {
        sysRoleEntity.setCreateBy(super.getCurrentUserId());
        sysRoleEntity.setUpdateBy(super.getCurrentUserId());
        return ResultUtil.ok(sysRoleService.add(sysRoleEntity));
    }

    /**
     * 修改角色
     *
     * @param sysRoleEntity 角色实体
     * @return ResultModel
     * @author wanyong
     * @date 2018-01-04 11:27
     */
    @ApiOperation(value = "修改角色", notes = "根据角色ID修改角色")
    @PostMapping("/modify")
    @RequiresPermissions("sys:role:update")
    @SysLogOpt(module = "角色管理", value = "角色修改", operationType = Constants.LogOptEnum.MODIFY)
    public ResultModel modify(@Valid @RequestBody SysRoleEntity sysRoleEntity) {
        sysRoleEntity.setUpdateBy(super.getCurrentUserId());
        sysRoleService.modifyById(sysRoleEntity);
        return ResultUtil.ok();
    }

    /**
     * 根据角色ID集合批量删除
     *
     * @param ids 角色ID集合
     * @return ResultModel
     * @author wanyong
     * @date 2017-12-23 02:46
     */
    @ApiOperation(value = "批量删除角色", notes = "根据主键ID集合批量删除角色")
    @PostMapping("/delBatchByIds")
    @RequiresPermissions("sys:role:delete")
    @SysLogOpt(module = "角色管理", value = "角色批量删除", operationType = Constants.LogOptEnum.DELETE)
    public ResultModel delBatchByIds(@RequestBody List<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            throw new BusinessException("角色ID集合不能为空");
        }
        return ResultUtil.ok(sysRoleService.deleteBatchIds(ids));
    }

    /**
     * 根据部门ID查询所属角色
     *
     * @param deptId 部门ID
     * @return ResultModel
     * @author wanyong
     * @date 2018-01-04 11:28
     */
    @ApiOperation(value = "查询角色", notes = "根据部门ID查询所属角色列表")
    @GetMapping("/queryRoles/{deptId}")
    @RequiresPermissions("sys:role:read")
    public ResultModel queryRoles(@PathVariable(value = "deptId") Long deptId) {
        Assert.notNull(deptId);
        return ResultUtil.ok(sysRoleService.queryRoles(deptId));
    }
}

