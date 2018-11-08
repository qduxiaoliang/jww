package com.jww.base.am.service.impl;

import com.jww.base.am.dao.mapper.SysRoleResourceMapper;
import com.jww.base.am.model.dto.SysRoleResourceDTO;
import com.jww.base.am.service.SysRoleResourceService;
import com.jww.common.core.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色授权表 服务实现类
 * </p>
 *
 * @author wanyong
 * @since 2017-12-17
 */
@Service("sysRoleResourceService")
public class SysRoleResourceServiceImpl extends BaseServiceImpl<SysRoleResourceMapper, SysRoleResourceDTO>
        implements SysRoleResourceService {

    @Override
    public boolean delBatchByRoleId(Long roleId) {
        return false;
    }
}
