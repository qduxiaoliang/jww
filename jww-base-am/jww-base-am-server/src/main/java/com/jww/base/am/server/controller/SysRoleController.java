package com.jww.base.am.server.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.dto.SysDeptDTO;
import com.jww.base.am.model.dto.SysRoleDTO;
import com.jww.base.am.server.annotation.SysLogOpt;
import com.jww.base.am.service.SysDeptService;
import com.jww.base.am.service.SysRoleService;
import com.jww.common.core.constant.enums.LogOptEnum;
import com.jww.common.core.exception.BusinessException;
import com.jww.common.web.BaseController;
import com.jww.common.web.model.dto.ResultDTO;
import com.jww.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
     * @return ResultDTO<SysRoleModel>
     * @author wanyong
     * @date 2017-12-05 13:35
     */
    @ApiOperation(value = "查询角色", notes = "根据角色主键ID查询角色")
    @ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "Long")
    @PostMapping("/query")
    // @RequiresPermissions("sys:role:read")
    public ResultDTO query(@RequestBody Long roleId) {
        Assert.notNull(roleId);
        SysRoleDTO sysRoleDTO = sysRoleService.getById(roleId);
        SysDeptDTO sysDeptDTO = sysDeptService.getById(sysRoleDTO.getDeptId());
        sysRoleDTO.setDeptName(sysDeptDTO.getDeptName());
        return ResultUtil.ok(sysRoleDTO);
    }

    /**
     * 分页查询角色列表
     *
     * @param page 分页实体
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-04 11:25
     */
    @ApiOperation(value = "分页查询角色列表", notes = "根据分页参数查询角色列表")
    @PostMapping("/listPage")
    // @RequiresPermissions("sys:role:read")
    public ResultDTO queryListPage(@RequestBody IPage<SysRoleDTO> page) {
        return ResultUtil.ok(sysRoleService.listPage(page));
    }

    /**
     * 新增角色
     *
     * @param sysRoleDTO 角色实体
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-04 11:26
     */
    @ApiOperation(value = "新增角色", notes = "根据角色实体新增角色")
    @PostMapping("/add")
    // @RequiresPermissions("sys:role:add")
    @SysLogOpt(module = "角色管理", value = "角色新增", operationType = LogOptEnum.ADD)
    public ResultDTO add(@Valid @RequestBody SysRoleDTO sysRoleDTO) {
        sysRoleDTO.setCreateBy(super.getCurrentUserId());
        sysRoleDTO.setUpdateBy(super.getCurrentUserId());
        return ResultUtil.ok(sysRoleService.save(sysRoleDTO));
    }

    /**
     * 修改角色
     *
     * @param sysRoleDTO 角色实体
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-04 11:27
     */
    @ApiOperation(value = "修改角色", notes = "根据角色ID修改角色")
    @PostMapping("/modify")
    // @RequiresPermissions("sys:role:update")
    @SysLogOpt(module = "角色管理", value = "角色修改", operationType = LogOptEnum.MODIFY)
    public ResultDTO modify(@Valid @RequestBody SysRoleDTO sysRoleDTO) {
        sysRoleDTO.setUpdateBy(super.getCurrentUserId());
        sysRoleService.updateById(sysRoleDTO);
        return ResultUtil.ok();
    }

    /**
     * 根据角色ID集合批量删除
     *
     * @param ids 角色ID集合
     * @return ResultDTO
     * @author wanyong
     * @date 2017-12-23 02:46
     */
    @ApiOperation(value = "批量删除角色", notes = "根据主键ID集合批量删除角色")
    @PostMapping("/delBatchByIds")
    // @RequiresPermissions("sys:role:delete")
    @SysLogOpt(module = "角色管理", value = "角色批量删除", operationType = LogOptEnum.DELETE)
    public ResultDTO delBatchByIds(@RequestBody List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            throw new BusinessException("角色ID集合不能为空");
        }
        return ResultUtil.ok(sysRoleService.removeByIds(ids));
    }

    /**
     * 根据部门ID查询所属角色
     *
     * @param deptId 部门ID
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-04 11:28
     */
    @ApiOperation(value = "查询角色", notes = "根据部门ID查询所属角色列表")
    @GetMapping("/queryRoles/{deptId}")
    // @RequiresPermissions("sys:role:read")
    public ResultDTO queryRoles(@PathVariable(value = "deptId") Long deptId) {
        Assert.notNull(deptId);
        return ResultUtil.ok(sysRoleService.list(deptId));
    }
}

