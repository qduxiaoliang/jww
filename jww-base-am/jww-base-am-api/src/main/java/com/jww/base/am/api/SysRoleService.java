package com.jww.base.am.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jww.base.am.model.entity.SysRoleEntity;
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
public interface SysRoleService extends BaseService<SysRoleEntity> {

    /**
     * 分页查找所有角色
     *
     * @param page
     * @return Page<SysRoleModel>
     * @author wanyong
     * @date 2017/12/4 14:45
     */
    Page<SysRoleEntity> queryListPage(Page<SysRoleEntity> page);


    /**
     * 新增角色
     *
     * @param sysRoleEntity
     * @return SysRoleModel
     * @author wanyong
     * @date 2017-12-19 15:37
     */
    SysRoleEntity add(SysRoleEntity sysRoleEntity);

    /**
     * 根据角色ID修改
     *
     * @param sysRoleEntity
     * @return SysRoleModel
     * @author wanyong
     * @date 2017-12-24 14:41
     */
    SysRoleEntity modifyById(SysRoleEntity sysRoleEntity);

    /**
     * 查询部门下所有角色
     *
     * @param deptId
     * @return List<SysRoleModel>
     * @author shadj
     * @date 2017/12/29 17:31
     */
    List<SysRoleEntity> queryRoles(Long deptId);
}
