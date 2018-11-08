package com.jww.base.am.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.dto.SysParamDTO;
import com.jww.common.core.base.BaseService;

/**
 * <p>
 * 全局参数表 服务类
 * </p>
 *
 * @author shadj
 * @since 2017-12-24
 */
public interface SysParamService extends BaseService<SysParamDTO> {

    /**
     * 分页查询参数配置明细
     *
     * @param page 分页对象
     * @return IPage<SysParamDTO>
     * @author shadj
     * @date 2017/12/24 14:45
     */
    IPage<SysParamDTO> listPage(IPage<SysParamDTO> page);

    /**
     * 根据参数key查询查询
     *
     * @param paramKey 参数key
     * @return SysParamDTO
     * @author wanyong
     * @date 2018-01-17 12:33
     */
    SysParamDTO getByParamKey(String paramKey);

}
