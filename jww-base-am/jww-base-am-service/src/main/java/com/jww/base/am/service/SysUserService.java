package com.jww.base.am.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.dto.SysUserDTO;
import com.jww.base.am.model.entity.SysRoleEntity;
import com.jww.base.am.model.entity.SysUserEntity;
import com.jww.base.am.model.entity.SysUserRoleEntity;
import com.jww.common.core.base.BaseService;

import java.util.List;

/**
 * 用户管理服务
 *
 * @author wanyong
 * @date 2017/11/17 16:43
 */
public interface SysUserService extends BaseService<SysUserEntity> {

    /**
     * 根据用户名查找用户
     *
     * @param username 账号
     * @return SysUserEntity
     * @author wanyong
     * @date 2017-12-05 12:48
     */
    SysUserEntity queryByUsername(String username);

    /**
     * 分页查找所有用户
     *
     * @param page 分页对象
     * @return Page<SysUserModel>
     * @author wanyong
     * @date 2017/12/4 14:45
     */
    IPage<SysUserEntity> queryListPage(IPage<SysUserEntity> page);

    /**
     * 根据ID集合批量删除
     *
     * @param ids 用户ID集合
     * @return boolean
     * @author wanyong
     * @date 2017-12-05 19:50
     */
    boolean delBatchByIds(List<Long> ids);

    /**
     * 根据部门ID查询角色
     *
     * @param deptId 部门ID
     * @return java.util.List<com.jww.ump.SysRoleModel>
     * @author RickyWang
     * @date 17/12/25 15:47:20
     */
    List<SysRoleEntity> queryRoles(Long deptId);

    /**
     * 根据用户查询用户角色关系
     *
     * @param userId 用户ID
     * @return java.util.List<com.jww.ump.SysUserRoleModel>
     * @author RickyWang
     * @date 17/12/25 21:20:55
     */
    List<SysUserRoleEntity> queryUserRoles(Long userId);

    /**
     * 修改
     *
     * @param sysUserDTO 用户传输实体
     * @return boolean
     * @author wanyong
     * @date 2017-12-27 12:09
     */
    boolean modify(SysUserDTO sysUserDTO);

    /**
     * 身份切换的用户列表
     *
     * @return List<SysUserModel>
     * @author shadj
     * @date 2018/1/20 20:16
     */
    List<SysUserEntity> queryRunasList();

    /**
     * 查询所有用户
     *
     * @return List<SysUserModel>
     * @author wanyong
     * @date 2018-01-24 13:49
     */
    List<SysUserEntity> queryList();

}
