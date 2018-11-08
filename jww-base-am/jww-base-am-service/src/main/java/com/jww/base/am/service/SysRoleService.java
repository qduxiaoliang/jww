package com.jww.base.am.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.dto.SysRoleDTO;
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
public interface SysRoleService extends BaseService<SysRoleDTO> {

    /**
     * 分页查找所有角色
     *
     * @param page 分页实体
     * @return Page<SysRoleModel>
     * @author wanyong
     * @date 2017/12/4 14:45
     */
    IPage<SysRoleDTO> listPage(IPage<SysRoleDTO> page);

    /**
     * 新增角色
     *
     * @param sysRoleDTO 角色传输实体
     * @return boolean
     * @author wanyong
     * @date 2017-12-19 15:37
     */
    @Override
    boolean save(SysRoleDTO sysRoleDTO);

    /**
     * 根据角色ID修改
     *
     * @param sysRoleDTO 角色传输实体
     * @return boolean
     * @author wanyong
     * @date 2017-12-24 14:41
     */
    @Override
    boolean updateById(SysRoleDTO sysRoleDTO);

    /**
     * 查询部门下所有角色
     *
     * @param deptId 部门ID
     * @return List<SysRoleDTO>
     * @author shadj
     * @date 2017/12/29 17:31
     */
    List<SysRoleDTO> list(Long deptId);

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
