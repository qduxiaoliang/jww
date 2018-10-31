package com.jww.base.am.server.controller;

import com.jww.base.am.api.SysUserService;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.model.SysUserModel;
import com.jww.base.am.model.SysUserRoleModel;
import com.jww.base.am.server.annotation.SysLogOpt;
import com.jww.common.core.Constants;
import com.jww.common.core.exception.BusinessException;
import com.jww.common.core.model.PageModel;
import com.jww.common.core.util.SecurityUtil;
import com.jww.common.web.BaseController;
import com.jww.common.web.model.ResultModel;
import com.jww.common.web.util.ResultUtil;
import com.jww.common.web.util.WebUtil;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
     * @return ResultModel<SysUserModel>
     * @author wanyong
     * @date 2017-12-05 13:35
     */
    @ApiOperation(value = "查询用户", notes = "根据用户主键ID查询用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @GetMapping("/query/{id}")
    @RequiresPermissions("sys:user:read")
    public ResultModel query(@PathVariable(value = "id") Long id) {
        Assert.notNull(id);
        SysUserModel sysUserModel = sysUserService.queryOne(id);
        sysUserModel.setPassword(null);
        return ResultUtil.ok(sysUserModel);
    }

    @ApiOperation(value = "查询所有用户")
    @PostMapping("/list")
    @RequiresPermissions("sys:user:read")
    public ResultModel queryList() {
        return ResultUtil.ok(sysUserService.queryList());
    }

    @ApiOperation(value = "查询可选择用户", notes = "管理员可选择所有用户，普通用户只能选择自己")
    @PostMapping("/selectUsers")
    @RequiresPermissions("sys:user:read")
    public ResultModel querySelectUsers() {
        Long currentUserId = super.getCurrentUserId();
        if (!AmConstants.USERID_ADMIN.equals(currentUserId)) {
            List<SysUserModel> list = new ArrayList<>();
            list.add((SysUserModel) super.getCurrentUser());
            return ResultUtil.ok(list);
        }
        return ResultUtil.ok(sysUserService.queryList());
    }

    /**
     * 分页查询用户列表
     *
     * @param pageModel 分页实体
     * @return ResultModel
     * @author wanyong
     * @date 2017/12/2 14:31
     */
    @ApiOperation(value = "分页查询用户列表", notes = "根据分页参数查询用户列表")
    @PostMapping("/listPage")
    @RequiresPermissions("sys:user:read")
    public ResultModel queryListPage(@RequestBody PageModel pageModel) {
        return ResultUtil.ok(sysUserService.queryListPage(pageModel));
    }

    /**
     * 新增用户
     *
     * @param sysUserModel 用户实体
     * @return ResultModel
     * @author wanyong
     * @date 2017-12-03 10:18
     */
    @ApiOperation(value = "新增用户", notes = "根据用户实体新增用户")
    @PostMapping("/add")
    @RequiresPermissions("sys:user:add")
    @SysLogOpt(module = "用户管理", value = "用户新增", operationType = Constants.LogOptEnum.ADD)
    public ResultModel add(@Valid @RequestBody SysUserModel sysUserModel) {
        SysUserModel existSysUserModel = sysUserService.queryByAccount(sysUserModel.getAccount());
        if (ObjectUtil.isNotNull(existSysUserModel)) {
            throw new BusinessException("已存在相同账号的用户");
        }
        if (StrUtil.isBlank(sysUserModel.getPassword()) || !AmConstants.USERID_ADMIN.equals(super.getCurrentUserId())) {
            // 设置初始密码: 123456
            sysUserModel.setPassword(SecurityUtil.encryptPassword("123456"));
        } else {
            sysUserModel.setPassword(SecurityUtil.encryptPassword(sysUserModel.getPassword()));
        }
        sysUserModel.setCreateBy(super.getCurrentUserId());
        sysUserService.add(sysUserModel);
        return ResultUtil.ok();
    }

    /**
     * 根据用户ID集合批量删除用户
     *
     * @param ids 用户ID集合
     * @return ResultModel
     * @author wanyong
     * @date 2018-01-04 11:32
     */
    @ApiOperation(value = "批量删除用户", notes = "根据主键ID集合批量删除用户")
    @PostMapping("/delBatchByIds")
    @RequiresPermissions("sys:user:delete")
    @SysLogOpt(module = "用户管理", value = "用户批量删除", operationType = Constants.LogOptEnum.DELETE)
    public ResultModel delBatchByIds(@RequestBody List<Long> ids) {
        if (ids.size() == 0) {
            throw new BusinessException("用户ID集合不能为空");
        }
        return ResultUtil.ok(sysUserService.delBatchByIds(ids));
    }

    /**
     * 修改用户
     *
     * @param sysUserModel 用户实体
     * @return ResultModel
     * @author wanyong
     * @date 2018-01-04 11:33
     */
    @ApiOperation(value = "修改用户", notes = "根据用户ID修改用户")
    @PostMapping("/modify")
    @RequiresPermissions("sys:user:update")
    @SysLogOpt(module = "用户管理", value = "用户修改", operationType = Constants.LogOptEnum.MODIFY)
    public ResultModel modify(@RequestBody SysUserModel sysUserModel) {
        sysUserModel.setCreateBy(super.getCurrentUserId());
        sysUserModel.setUpdateTime(new Date());
        sysUserModel.setAccount(null);
        if (StrUtil.isNotBlank(sysUserModel.getPassword()) && AmConstants.USERID_ADMIN.equals(super.getCurrentUserId())) {
            sysUserModel.setPassword(SecurityUtil.encryptPassword(sysUserModel.getPassword()));
        } else {
            sysUserModel.setPassword(null);
        }
        return ResultUtil.ok(sysUserService.modifyUser(sysUserModel));
    }

    /**
     * 个人资料修改
     *
     * @param sysUserModel 用户实体
     * @return ResultModel
     * @author wanyong
     * @date 2018-01-04 11:33
     */
    @ApiOperation(value = "修改个人资料", notes = "根据用户ID修改用户个人资料")
    @PostMapping("/modifyMySelf")
    @SysLogOpt(module = "用户管理", value = "个人资料修改", operationType = Constants.LogOptEnum.MODIFY)
    public ResultModel modifyMySelf(@RequestBody SysUserModel sysUserModel) {
        if (!sysUserModel.getId().equals(WebUtil.getCurrentUserId())) {
            throw new BusinessException("不能修改其他用户信息");
        }
        sysUserModel.setCreateBy(super.getCurrentUserId());
        sysUserModel.setUpdateTime(new Date());
        sysUserModel.setAccount(null);
        return ResultUtil.ok(sysUserService.modifyById(sysUserModel));
    }

    /**
     * 根据用户id查询用户角色关系
     *
     * @param userId 用户ID
     * @return com.jww.common.web.model.ResultModel
     * @author RickyWang
     * @date 17/12/25 21:26:57
     */
    @ApiOperation(value = "查询用户角色关系", notes = "根据用户id查询用户角色关系")
    @GetMapping("/queryUserRoles/{userId}")
    @RequiresPermissions("sys:user:read")
    public ResultModel queryUserRoles(@PathVariable(value = "userId") Long userId) {
        Assert.notNull(userId);
        List<SysUserRoleModel> list = sysUserService.queryUserRoles(userId);
        return ResultUtil.ok(list);
    }

    /**
     * 修改密码
     *
     * @param sysUserModel 用户实体
     * @return ResultModel
     * @author wanyong
     * @date 2017/12/30 22:18
     */
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @PostMapping("/modifyPassword")
    @RequiresPermissions("sys:user:update")
    @SysLogOpt(module = "用户管理", value = "修改密码", operationType = Constants.LogOptEnum.MODIFY)
    public ResultModel modifyPassword(@RequestBody SysUserModel sysUserModel) {
        Assert.notEmpty(sysUserModel.getOldPassword());
        Assert.notEmpty(sysUserModel.getPassword());
        String encryptOldPassword = SecurityUtil.encryptPassword(sysUserModel.getOldPassword());
        SysUserModel currentSysUserModel = sysUserService.queryById(super.getCurrentUserId());
        if (!encryptOldPassword.equals(currentSysUserModel.getPassword())) {
            throw new BusinessException("旧密码不正确");
        }
        String encryptPassword = SecurityUtil.encryptPassword(sysUserModel.getPassword());
        sysUserModel.setPassword(encryptPassword);
        sysUserModel.setId(super.getCurrentUserId());
        sysUserModel.setAccount(null);
        return ResultUtil.ok(sysUserService.modifyById(sysUserModel));
    }

}
