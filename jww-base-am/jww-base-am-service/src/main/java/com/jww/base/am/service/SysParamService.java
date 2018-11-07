package com.jww.base.am.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.dos.SysParamDO;
import com.jww.common.core.base.BaseService;

/**
 * <p>
 * 全局参数表 服务类
 * </p>
 *
 * @author shadj
 * @since 2017-12-24
 */
public interface SysParamService extends BaseService<SysParamDO> {

    /**
     * 分页查询参数配置明细
     *
     * @param page
     * @return Page<SysParamModel>
     * @author shadj
     * @date 2017/12/24 14:45
     */
    IPage<SysParamDO> queryListPage(IPage<SysParamDO> page);

    /**
     * 根据参数key查询查询
     *
     * @param paramKey
     * @return SysParamModel
     * @author wanyong
     * @date 2018-01-17 12:33
     */
    SysParamDO queryByParamKey(String paramKey);

}
