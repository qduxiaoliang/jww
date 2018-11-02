package com.jww.base.am.server.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.entity.SysLogEntity;
import com.jww.base.am.service.SysLogService;
import com.jww.common.web.BaseController;
import com.jww.common.web.model.dto.ResultDTO;
import com.jww.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author wanyong
 * @since 2017-12-26
 */
@RestController
@RequestMapping("/log")
@Slf4j
@Api(value = "日志管理", description = "系统日志管理")
public class SysLogController extends BaseController {
    @Autowired
    private SysLogService sysLogService;

    /**
     * 查询日志分页方法
     *
     * @param page
     * @return com.jww.common.web.model.dto.ResultDTO
     * @author RickyWang
     * @date 17/12/26 12:28:13
     */
    @ApiOperation(value = "分页查询日志列表", notes = "根据分页参数查询日志列表")
    @PostMapping("/queryListPage")
    // @RequiresPermissions("sys:log:read")
    public ResultDTO queryListPage(@RequestBody IPage<SysLogEntity> page) {
        return ResultUtil.ok(sysLogService.queryListPage(page));
    }
}

