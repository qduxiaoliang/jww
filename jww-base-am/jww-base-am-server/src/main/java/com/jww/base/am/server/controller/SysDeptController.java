package com.jww.base.am.server.controller;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.dos.SysDeptDO;
import com.jww.common.core.model.dto.TreeDTO;
import com.jww.base.am.server.annotation.SysLogOpt;
import com.jww.base.am.service.SysDeptService;
import com.jww.common.core.constant.enums.LogOptEnum;
import com.jww.common.web.BaseController;
import com.jww.common.web.model.dto.ResultDTO;
import com.jww.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 部门管理控制器
 *
 * @author Ricky Wang
 * @date 17/12/1 11:23:17
 */
@RestController
@RequestMapping("/dept")
@Api(value = "部门管理", description = "部门管理")
public class SysDeptController extends BaseController {

    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 根据部门ID查询
     *
     * @param id 部门ID
     * @return ResultDTO<SysDeptModel>
     * @author RickyWang
     * @date 2017-12-05 13:35
     */
    @ApiOperation(value = "查询部门", notes = "根据部门主键ID查询部门")
    @ApiImplicitParam(name = "id", value = "部门ID", required = true, dataType = "Long")
    @GetMapping("/query/{id}")
    // @RequiresPermissions("sys:dept:read")
    public ResultDTO query(@PathVariable Long id) {
        Assert.notNull(id);
        SysDeptDO sysDeptDO = sysDeptService.queryOne(id);
        return ResultUtil.ok(sysDeptDO);
    }

    /**
     * 查询部门分页方法
     *
     * @param pageModel 分页实体
     * @return com.jww.common.web.model.dto.ResultDTO
     * @author RickyWang
     * @date 17/12/25 21:28:13
     */
    @ApiOperation(value = "分页查询部门列表", notes = "根据分页参数查询部门列表")
    @PostMapping("/queryListPage")
    // @RequiresPermissions("sys:dept:read")
    public ResultDTO queryListPage(@RequestBody IPage<SysDeptDO> pageModel) {
        pageModel = sysDeptService.queryListPage(pageModel);
        return ResultUtil.ok(pageModel);
    }

    /**
     * 新增部门方法
     *
     * @param sysDeptDO 部门实体
     * @return com.jww.common.web.model.dto.ResultDTO
     * @author RickyWang
     * @date 17/12/25 21:28:41
     */
    @ApiOperation(value = "新增部门", notes = "根据部门实体新增部门")
    @PostMapping("/add")
    // @RequiresPermissions("sys:dept:add")
    @SysLogOpt(module = "部门管理", value = "部门新增", operationType = LogOptEnum.ADD)
    public ResultDTO add(@Valid @RequestBody SysDeptDO sysDeptDO) {
        if (sysDeptDO != null) {
            sysDeptDO.setCreateBy(super.getCurrentUserId());
            sysDeptDO.setUpdateBy(super.getCurrentUserId());
        }
        return ResultUtil.ok(sysDeptService.addDept(sysDeptDO));
    }

    /**
     * 修改部门方法
     *
     * @param sysDeptDO 部门实体
     * @return com.jww.common.web.model.dto.ResultDTO
     * @author RickyWang
     * @date 17/12/25 21:29:09
     */
    @ApiOperation(value = "修改部门", notes = "根据主键ID修改部门")
    @PutMapping("/modify")
    // @RequiresPermissions("sys:dept:update")
    @SysLogOpt(module = "部门管理", value = "部门修改", operationType = LogOptEnum.MODIFY)
    public ResultDTO modify(@RequestBody SysDeptDO sysDeptDO) {
        sysDeptDO.setUpdateBy(super.getCurrentUserId());
        sysDeptDO.setUpdateTime(new Date());
        sysDeptService.modifyById(sysDeptDO);
        return ResultUtil.ok();
    }

    /**
     * 批量删除部门方法
     *
     * @param ids 部门ID集合
     * @return com.jww.common.web.model.dto.ResultDTO
     * @author RickyWang
     * @date 17/12/25 21:29:23
     */
    @ApiOperation(value = "批量删除部门", notes = "根据主键ID集合批量删除部门")
    @DeleteMapping("/delBatchByIds")
    // @RequiresPermissions("sys:dept:delete")
    @SysLogOpt(module = "部门管理", value = "部门批量删除", operationType = LogOptEnum.DELETE)
    public ResultDTO delBatchByIds(@RequestBody Long[] ids) {
        Assert.notNull(ids);
        return ResultUtil.ok(sysDeptService.deleteBatch(ids));
    }

    /**
     * 根据部门id过滤查询部门树方法
     *
     * @param id 部门ID
     * @return com.jww.common.web.model.dto.ResultDTO
     * @author RickyWang
     * @date 17/12/25 21:29:52
     */
    @ApiOperation(value = "查询部门树", notes = "根据部门ID查询部门树结构数据")
    @GetMapping("/queryTree/{id}")
    // @RequiresPermissions("sys:dept:read")
    public ResultDTO queryTree(@PathVariable(value = "id", required = false) Long id) {
        List<TreeDTO> list = sysDeptService.queryTree(id);
        return ResultUtil.ok(list);
    }

    /**
     * 查询部门树方法
     *
     * @return com.jww.common.web.model.dto.ResultDTO
     * @author RickyWang
     * @date 17/12/25 21:30:28
     */
    @ApiOperation(value = "查询部门树", notes = "查询全部部门树结构数据")
    @GetMapping("/queryTree")
    // @RequiresPermissions("sys:dept:read")
    public ResultDTO queryTree() {
        List<TreeDTO> list = sysDeptService.queryTree();
        return ResultUtil.ok(list);
    }

    /**
     * 删除部门方法
     *
     * @param id 部门ID
     * @return com.jww.common.web.model.dto.ResultDTO
     * @author RickyWang
     * @date 17/12/25 21:30:45
     */
    @ApiOperation(value = "删除部门", notes = "根据部门ID删除部门")
    @DeleteMapping("/delDept")
    // @RequiresPermissions("sys:dept:delete")
    @SysLogOpt(module = "部门管理", value = "部门删除", operationType = LogOptEnum.DELETE)
    public ResultDTO delDept(@RequestBody Long id) {
        Assert.notNull(id);
        return ResultUtil.ok(sysDeptService.delDept(id));
    }

}
