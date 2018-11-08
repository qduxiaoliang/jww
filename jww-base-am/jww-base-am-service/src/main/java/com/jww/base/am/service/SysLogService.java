package com.jww.base.am.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.dto.SysLogDTO;
import com.jww.common.core.base.BaseService;

/**
 * 日志业务类
 *
 * @author wanyong
 * @date 2018-11-8 15:05
 */
public interface SysLogService extends BaseService<SysLogDTO> {
    /**
     * 分页查询日志
     *
     * @param page 分页实体
     * @return IPage<SysLogDTO>
     * @author RickyWang
     * @date 18/1/1 15:03:43
     */
    IPage<SysLogDTO> listPage(IPage<SysLogDTO> page);

    /**
     * 清除日志
     *
     * @param keepDays 保留天数
     * @return boolean
     * @author RickyWang
     * @date 18/1/1 15:03:43
     */
    boolean clearLog(Integer keepDays);
}
