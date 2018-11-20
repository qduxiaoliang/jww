package com.jww.base.am.service;

import com.jww.base.am.model.dos.SysRoleResourceDO;
import com.jww.common.core.base.BaseService;

/**
 * <p>
 * 角色授权表 服务类
 * </p>
 *
 * @author wanyong
 * @since 2017-12-17
 */
public interface SysRoleResourceService extends BaseService<SysRoleResourceDO> {

    /**
     * 根据角色ID批量删除角色和菜单的关系
     *
     * @param roleId
     * @return boolean
     * @author wanyong
     * @date 2017-12-24 15:01
     */
    boolean delBatchByRoleId(Long roleId);
}
