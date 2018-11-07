package com.jww.base.am.server.controller;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.dos.SysDicDO;
import com.jww.base.am.server.annotation.SysLogOpt;
import com.jww.base.am.service.SysDicService;
import com.jww.common.core.constant.enums.LogOptEnum;
import com.jww.common.core.exception.BusinessException;
import com.jww.common.web.BaseController;
import com.jww.common.web.model.dto.ResultDTO;
import com.jww.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 字典管理 前端控制器
 * </p>
 *
 * @author wanyong
 * @since 2017-11-17
 */
@RestController
@RequestMapping("/dic")
@Api(value = "字典管理", description = "字典管理")
public class SysDicController extends BaseController {

    @Autowired
    private SysDicService sysDicService;

    /**
     * 根据字典ID查询字典
     *
     * @param dicId 字典主键
     * @return ResultDTO<SysDicModel>
     * @author wanyong
     * @date 2017-12-05 13:35
     */
    @ApiOperation(value = "查询字典", notes = "根据字典主键ID查询字典")
    @ApiImplicitParam(name = "id", value = "字典ID", required = true, dataType = "Long")
    @PostMapping("/query")
    // @RequiresPermissions("sys:dic:read")
    public ResultDTO query(@RequestBody Long dicId) {
        Assert.notNull(dicId);
        SysDicDO sysDicDO = sysDicService.getById(dicId);
        return ResultUtil.ok(sysDicDO);
    }

    /**
     * 分页查询字典列表
     *
     * @param page 分页实体
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-03 12:53
     */
    @ApiOperation(value = "分页查询字典", notes = "根据字典主键ID查询字典")
    @PostMapping("/listPage")
    // @RequiresPermissions("sys:dic:read")
    public ResultDTO queryListPage(@RequestBody IPage page) {
        return ResultUtil.ok(sysDicService.queryListPage(page));
    }

    /**
     * 新增字典
     *
     * @param sysDicDO 字典实体
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-03 13:28
     */
    @ApiOperation(value = "新增字典", notes = "根据字典实体新增字典")
    @PostMapping("/add")
    // @RequiresPermissions("sys:dic:add")
    @SysLogOpt(module = "字典管理", value = "字典新增", operationType = LogOptEnum.ADD)
    public ResultDTO add(@Valid @RequestBody SysDicDO sysDicDO) {
        sysDicDO.setCreateBy(super.getCurrentUserId());
        sysDicDO.setUpdateBy(super.getCurrentUserId());
        return ResultUtil.ok(sysDicService.add(sysDicDO));
    }

    /**
     * 修改字典
     *
     * @param sysDicDO 字典实体
     * @return com.jww.common.web.model.dto.ResultDTO
     * @author RickyWang
     * @date 17/12/25 21:29:09
     */
    @ApiOperation(value = "修改字典", notes = "根据字典ID修改字典")
    @PostMapping("/modify")
    // @RequiresPermissions("sys:dic:update")
    @SysLogOpt(module = "字典管理", value = "字典修改", operationType = LogOptEnum.MODIFY)
    public ResultDTO modify(@Valid @RequestBody SysDicDO sysDicDO) {
        sysDicDO.setUpdateBy(super.getCurrentUserId());
        sysDicService.modifyById(sysDicDO);
        return ResultUtil.ok();
    }

    /**
     * 根据字典ID集合批量删除
     *
     * @param ids 主键集合
     * @return ResultDTO
     * @author wanyong
     * @date 2017-12-23 02:46
     */
    @ApiOperation(value = "批量删除字典", notes = "根据主键ID集合批量删除字典")
    @PostMapping("/delBatchByIds")
    // @RequiresPermissions("sys:dic:delete")
    @SysLogOpt(module = "字典管理", value = "字典批量删除", operationType = LogOptEnum.DELETE)
    public ResultDTO delBatchByIds(@RequestBody List<Long> ids) {
        if (ids.size() == 0) {
            throw new BusinessException("字典ID集合不能为空");
        }
        return ResultUtil.ok(sysDicService.removeByIds(ids));
    }

    /**
     * 查询字典类型列表
     *
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-10 13:00
     */
    @ApiOperation(value = "查询字典类型列表", notes = "查询字典类型列表")
    @PostMapping("/typeList")
    // @RequiresPermissions("sys:dic:read")
    public ResultDTO queryTypeList() {
        return ResultUtil.ok(sysDicService.queryTypeList());
    }

    /**
     * 根据字典类型值查询字典集合
     *
     * @param type 字典类型值
     * @return ResultDTO
     * @author wanyong
     * @date 2018-01-10 18:14
     */
    @ApiOperation(value = "查询字典集合", notes = "根据字典类型值查询字典集合")
    @PostMapping("/listByType")
    // @RequiresPermissions("sys:dic:read")
    public ResultDTO queryListByType(@RequestBody String type) {
        Assert.notBlank(type);
        return ResultUtil.ok(sysDicService.queryListByType(type));
    }
}

