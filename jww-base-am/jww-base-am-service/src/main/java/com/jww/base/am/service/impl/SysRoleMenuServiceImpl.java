package com.jww.base.am.service.impl;

import com.jww.common.core.base.BaseServiceImpl;
import com.jww.base.am.dao.mapper.SysRoleMenuMapper;
import com.jww.base.am.model.entity.SysRoleMenuEntity;
import com.jww.base.am.api.SysRoleMenuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色授权表 服务实现类
 * </p>
 *
 * @author wanyong
 * @since 2017-12-17
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends BaseServiceImpl<SysRoleMenuMapper, SysRoleMenuEntity> implements SysRoleMenuService {

    @Override
    public boolean delBatchByRoleId(Long roleId) {
        return false;
    }
}
