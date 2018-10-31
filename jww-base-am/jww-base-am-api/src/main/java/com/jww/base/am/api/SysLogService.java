package com.jww.base.am.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jww.base.am.model.entity.SysLogEntity;
import com.jww.common.core.base.BaseService;

/**
 * 日志业务类
 *
 * @author RickyWang
 * @date 17/12/26 12:41:05
 */
public interface SysLogService extends BaseService<SysLogEntity> {
    /**
     * 分页查询日志
     *
     * @param page
     * @return com.baomidou.mybatisplus.plugins.Page<com.jww.ump.SysLogModel>
     * @author RickyWang
     * @date 18/1/1 15:03:43
     */
    public Page<SysLogEntity> queryListPage(Page<SysLogEntity> page);

    /**
     * 清除日志
     *
     * @param keepDays 保留天数
     * @return boolean
     * @author RickyWang
     * @date 18/1/1 15:03:43
     */
    public boolean clearLog(Integer keepDays);
}
