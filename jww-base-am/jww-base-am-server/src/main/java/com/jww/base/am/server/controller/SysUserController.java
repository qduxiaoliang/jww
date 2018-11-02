package com.jww.base.am.server.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.model.entity.SysUserEntity;
import com.jww.base.am.model.entity.SysUserRoleEntity;
import com.jww.base.am.server.annotation.SysLogOpt;
import com.jww.base.am.service.SysUserService;
import com.jww.common.core.constant.enums.LogOptEnum;
import com.jww.common.core.exception.BusinessException;
import com.jww.common.core.util.SecurityUtil;
import com.jww.common.web.BaseController;
import com.jww.common.web.model.dto.ResultDTO;
import com.jww.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户管理控制器
 *
 * @author wanyong
 * @date 2017/11/17 00:22
 */
@Slf4j
@Api(value = "用户管理", description = "用户管理")
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 根据用户ID查询用户
     *
     * @param id
     * @return ResultDTO<SysUserModel>
     * @author wanyong
     * @date 2017-12-05 13:35
     */
    @ApiOperation(value = "查询用户", notes = "根据用户主键ID查询用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @GetMapping("/query/{id}")
    // @RequiresPermissions("sys:user:read")
    public ResultDTO query(@PathVariable(value = "id") Long id) {
        Assert.notNull(id);
        SysUserEntity sysUserEntity = sysUserService.queryOne(id);
        sysUserEntity.setPassword(null);
        return ResultUtil.ok(sysUserEntity);
    }

    @ApiOperation(value = "查询所有用户")
    @PostMapping("/list")
    // @RequiresPermissions("sys:user:read")
    public ResultDTO queryList() {
        return ResultUtil.ok(sysUserService.queryList());
    }

    @ApiOperation(value = "查询可选择用户", notes = "管理员可选择所有用户，普通用户只能选择自己")
    @PostMapping("/selectUsers")
    // @RequiresPermissions("sys:user:read")
    public ResultDTO querySelectUsers() {
        Long currentUserId = super.getCurrentUserId();
        if (!AmConstants.USERID_ADMIN.equals(currentUserId)) {
            List<SysUserEntity> list = new ArrayList<>();
            list.add((SysUserEntity) super.getCurrentUser());
            return ResultUtil.ok(list);
        }
        return ResultUtil.ok(sysUserService.queryList());
    }

    /**
     * 分页查询用户列表
     *
     * @param page 分页实体
     * @return ResultDTO
     * @author wanyong
     * @date 2017/12/2 14:31
     */
    @ApiOperation(value = "分页查询用户列表", notes = "根据分页参数查询用户列表")
    @PostMapping("/listPage")
    // @RequiresPermissions("sys:user:read")
    public ResultDTO queryListPage(@RequestBody IPage page) {
        return ResultUtil.ok(sysUserService.queryListPage(page));
    }

    /**
     * 新增用户
     *
     * @param sysUserEntity 用户实体
     * @return ResultDTO
     * @author wanyong
     * @date 2017-12-03 10:18
     */
    @ApiOperation(value = "新增用户", notes = "根据用户实体新增用户")
    @PostMapping("/add")
    // @RequiresPermissions("sys:user:add")
    @SysLogOpt(module = "用户管理", value = "用户新增", operationType = LogOptEnum.ADD)
    public ResultDTO add(@Valid @RequestBody SysUserEntity sysUserEntity) {
        SysUserEntity existSysUserEntity = sysUserService.queryByAccount(sysUserEntity.getAccount());
        if (ObjectUtil.isNotNull(existSysUserEntity)) {
            throw new BusinessException("已存在相同账号的用户");
        }
        if (StrUtil.isBlank(sysUserEntity.getPassword()) || !AmConstants.USERID_ADMIN.equals(super.getCurrentUserId())) {
            // 设置初始密码: 123456
            sysUserEntity.setPassword(SecurityUtil.encryptPassword("123456"));
        } else {
            sysUserEntity.setPassword(SecurityUtil.encryptPassword(sysUserEntity.getPassword()));
        }
        sysUserEntity.setCreateBy(super.getCurrentUserId());
        sysUserService.add(sysUserEntity);
        return ResultUtil.ok();
    }

    /**
     * 根据用户ID集合批量删除用户
     *
     * @param ids 用户ID集合
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-04 11:32
     */
    @ApiOperation(value = "批量删除用户", notes = "根据主键ID集合批量删除用户")
    @PostMapping("/delBatchByIds")
    // @RequiresPermissions("sys:user:delete")
    @SysLogOpt(module = "用户管理", value = "用户批量删除", operationType = LogOptEnum.DELETE)
    public ResultDTO delBatchByIds(@RequestBody List<Long> ids) {
        if (ids.size() == 0) {
            throw new BusinessException("用户ID集合不能为空");
        }
        return ResultUtil.ok(sysUserService.delBatchByIds(ids));
    }

    /**
     * 修改用户
     *
     * @param sysUserEntity 用户实体
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-04 11:33
     */
    @ApiOperation(value = "修改用户", notes = "根据用户ID修改用户")
    @PostMapping("/modify")
    // @RequiresPermissions("sys:user:update")
    @SysLogOpt(module = "用户管理", value = "用户修改", operationType = LogOptEnum.MODIFY)
    public ResultDTO modify(@RequestBody SysUserEntity sysUserEntity) {
        sysUserEntity.setCreateBy(super.getCurrentUserId());
        sysUserEntity.setUpdateTime(new Date());
        sysUserEntity.setAccount(null);
        if (StrUtil.isNotBlank(sysUserEntity.getPassword()) && AmConstants.USERID_ADMIN.equals(super.getCurrentUserId())) {
            sysUserEntity.setPassword(SecurityUtil.encryptPassword(sysUserEntity.getPassword()));
        } else {
            sysUserEntity.setPassword(null);
        }
        return ResultUtil.ok(sysUserService.modifyUser(sysUserEntity));
    }

    /**
     * 个人资料修改
     *
     * @param sysUserEntity 用户实体
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-04 11:33
     */
    @ApiOperation(value = "修改个人资料", notes = "根据用户ID修改用户个人资料")
    @PostMapping("/modifyMySelf")
    @SysLogOpt(module = "用户管理", value = "个人资料修改", operationType = LogOptEnum.MODIFY)
    public ResultDTO modifyMySelf(@RequestBody SysUserEntity sysUserEntity) {
        // if (!sysUserEntity.getId().equals(WebUtil.getCurrentUserId())) {
        if (!sysUserEntity.getId().equals(null)) {
            throw new BusinessException("不能修改其他用户信息");
        }
        sysUserEntity.setCreateBy(super.getCurrentUserId());
        sysUserEntity.setUpdateTime(new Date());
        sysUserEntity.setAccount(null);
        return ResultUtil.ok(sysUserService.modifyById(sysUserEntity));
    }

    /**
     * 根据用户id查询用户角色关系
     *
     * @param userId 用户ID
     * @return com.jww.common.web.model.dto.ResultDTO
     * @author RickyWang
     * @date 17/12/25 21:26:57
     */
    @ApiOperation(value = "查询用户角色关系", notes = "根据用户id查询用户角色关系")
    @GetMapping("/queryUserRoles/{userId}")
    // @RequiresPermissions("sys:user:read")
    public ResultDTO queryUserRoles(@PathVariable(value = "userId") Long userId) {
        Assert.notNull(userId);
        List<SysUserRoleEntity> list = sysUserService.queryUserRoles(userId);
        return ResultUtil.ok(list);
    }

    /**
     * 修改密码
     *
     * @param sysUserEntity 用户实体
     * @return ResultDTO
     * @author wanyong
     * @date 2017/12/30 22:18
     */
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @PostMapping("/modifyPassword")
    // @RequiresPermissions("sys:user:update")
    @SysLogOpt(module = "用户管理", value = "修改密码", operationType = LogOptEnum.MODIFY)
    public ResultDTO modifyPassword(@RequestBody SysUserEntity sysUserEntity) {
        Assert.notEmpty(sysUserEntity.getOldPassword());
        Assert.notEmpty(sysUserEntity.getPassword());
        String encryptOldPassword = SecurityUtil.encryptPassword(sysUserEntity.getOldPassword());
        SysUserEntity currentSysUserEntity = sysUserService.getById(super.getCurrentUserId());
        if (!encryptOldPassword.equals(currentSysUserEntity.getPassword())) {
            throw new BusinessException("旧密码不正确");
        }
        String encryptPassword = SecurityUtil.encryptPassword(sysUserEntity.getPassword());
        sysUserEntity.setPassword(encryptPassword);
        sysUserEntity.setId(super.getCurrentUserId());
        sysUserEntity.setAccount(null);
        return ResultUtil.ok(sysUserService.modifyById(sysUserEntity));
    }

}
