package com.jww.base.am.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.dos.SysRoleDO;
import com.jww.common.core.base.BaseService;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author wanyong
 * @since 2017-12-17
 */
public interface SysRoleService extends BaseService<SysRoleDO> {

    /**
     * 分页查找所有角色
     *
     * @param page 分页实体
     * @return Page<SysRoleDO>
     * @author wanyong
     * @date 2017/12/4 14:45
     */
    IPage<SysRoleDO> listPage(IPage<SysRoleDO> page);

    /**
     * 查询部门下所有角色
     *
     * @param deptId 部门ID
     * @return List<SysRoleDO>
     * @author shadj
     * @date 2017/12/29 17:31
     */
    List<SysRoleDO> list(Long deptId);

    /**
     * 根据角色ID集合批量删除
     *
     * @param idList 角色ID集合s
     * @return boolean
     * @author wanyong
     * @date 2018-11-8 10:57
     */
    boolean removeByIds(List<Long> idList);
}
