package com.jww.base.am.server.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.entity.SysParamEntity;
import com.jww.base.am.server.annotation.SysLogOpt;
import com.jww.base.am.service.SysParamService;
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
 * 参数管理 前端控制器
 * </p>
 *
 * @author shadj
 * @since 2017-12-24
 */
@RestController
@RequestMapping("/param")
@Api(value = "参数管理", description = "参数管理")
public class SysParamController extends BaseController {

    @Autowired
    private SysParamService sysParamService;

    /**
     * 根据参数ID查询参数
     *
     * @param paramId 参数主键
     * @return ResultDTO<SysParamModel>
     * @author shadj
     * @date 2017-12-05 13:35
     */
    @ApiOperation(value = "查询参数", notes = "根据参数主键ID查询参数")
    @ApiImplicitParam(name = "id", value = "参数主键ID", required = true, dataType = "Long")
    @PostMapping("/query")
    // @RequiresPermissions("sys:param:read")
    public ResultDTO query(@RequestBody Long paramId) {
        Assert.notNull(paramId);
        SysParamEntity sysParamEntity = sysParamService.getById(paramId);
        return ResultUtil.ok(sysParamEntity);
    }

    /**
     * 分页查询参数列表
     *
     * @param page 分页对象
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-03 19:27
     */
    @ApiOperation(value = "分页查询参数", notes = "根据分页参数查询参数列表")
    @PostMapping("/listPage")
    // @RequiresPermissions("sys:param:read")
    public ResultDTO queryListPage(@RequestBody IPage page) {
        return ResultUtil.ok(sysParamService.queryListPage(page));
    }

    /**
     * 新增参数
     *
     * @param sysParamEntity 参数实体
     * @return ResultDTO
     * @author shadj
     * @date 2017/12/18 21:54
     */
    @ApiOperation(value = "新增参数", notes = "根据参数实体新增参数")
    @PostMapping("/add")
    // @RequiresPermissions("sys:param:add")
    @SysLogOpt(module = "参数管理", value = "参数新增", operationType = LogOptEnum.ADD)
    public ResultDTO add(@Valid @RequestBody SysParamEntity sysParamEntity) {
        sysParamEntity.setCreateBy(super.getCurrentUserId());
        sysParamEntity.setUpdateBy(super.getCurrentUserId());
        return ResultUtil.ok(sysParamService.add(sysParamEntity));
    }

    /**
     * 修改参数
     *
     * @param sysParamEntity 参数实体
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-03 22:36
     */
    @ApiOperation(value = "修改参数", notes = "根据参数ID修改参数")
    @PostMapping("/modify")
    // @RequiresPermissions("sys:param:update")
    @SysLogOpt(module = "参数管理", value = "参数修改", operationType = LogOptEnum.MODIFY)
    public ResultDTO modify(@RequestBody SysParamEntity sysParamEntity) {
        sysParamEntity.setUpdateBy(super.getCurrentUserId());
        sysParamService.modifyById(sysParamEntity);
        return ResultUtil.ok();
    }

    /**
     * 根据参数ID集合批量删除
     *
     * @param ids 主键集合
     * @return ResultDTO
     * @author shadj
     * @date 2017-12-24 18:30
     */
    @ApiOperation(value = "批量删除参数", notes = "根据主键ID集合批量删除参数")
    @DeleteMapping("/deleteBatchByIds")
    // @RequiresPermissions("sys:param:delete")
    @SysLogOpt(module = "参数管理", value = "参数批量删除", operationType = LogOptEnum.DELETE)
    public ResultDTO deleteBatchByIds(@RequestBody List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            throw new BusinessException("参数ID集合不能为空");
        }
        return ResultUtil.ok(sysParamService.removeByIds(ids));
    }
}

