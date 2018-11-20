package com.jww.base.am.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.dos.SysDeptDO;
import com.jww.common.core.base.BaseService;
import com.jww.common.core.model.dto.TreeDTO;

import java.util.List;

/**
 * 部门管理服务接口
 *
 * @author Ricky Wang
 * @date 2017-12-27 12:02
 */
public interface SysDeptService extends BaseService<SysDeptDO> {

    /**
     * 新增
     *
     * @param sysDeptDO 部门传输实体
     * @return boolean
     * @author wanyong
     * @date 2018-11-8 15:44
     */
    @Override
    boolean save(SysDeptDO sysDeptDO);

    /**
     * 根据部门ID修改
     *
     * @param sysDeptDO 部门传输实体
     * @return boolean
     * @author wanyong
     * @date 2018-11-8 15:44
     */
    @Override
    boolean updateById(SysDeptDO sysDeptDO);

    /**
     * 分页查询部门
     *
     * @param page 分页实体
     * @return IPage<SysDeptDO>
     * @author wanyong
     * @date 2018-11-8 15:44
     */
    IPage<SysDeptDO> listPage(IPage<SysDeptDO> page);

    /**
     * 查询单个部门
     *
     * @param deptId 部门ID
     * @return SysDeptDO
     * @author RickyWang
     * @date 18/1/1 15:05:53
     */
    SysDeptDO getOne(Long deptId);

    /**
     * 批量删除部门
     *
     * @param ids
     * @return boolean
     * @author RickyWang
     * @date 18/1/1 15:06:11
     */

    /**
     * 根据部门ID集合批量删除
     *
     * @param ids 部门ID集合
     * @return boolean
     * @author wanyong
     * @date 2018-11-8 16:15
     */
    boolean removeByIds(List<Long> ids);

    /**
     * 查询部门树
     *
     * @return List<TreeDTO>
     * @author wanyong
     * @date 2018-11-8 16:15
     */
    List<TreeDTO> listTree();

    /**
     * 查询部门树，过滤该id下所有部门
     *
     * @param deptId 部门ID
     * @return List<TreeDTO>
     * @author wanyong
     * @date 2018-11-8 16:15
     */
    List<TreeDTO> ListTreeExcludeId(Long deptId);

    /**
     * 删除部门
     *
     * @param deptId 部门ID
     * @return boolean
     * @author wanyong
     * @date 2018-11-8 16:15
     */
    boolean removeById(Long deptId);

    /**
     * 按id查询子部门
     *
     * @param id
     * @return List<SysDeptDO>
     * @author wanyong
     * @date 2018-11-8 16:15
     */
    List<SysDeptDO> listChild(Long id);

    /**
     * 查询子部门数量
     *
     * @param id
     * @return int
     * @author RickyWang
     * @date 18/1/1 15:08:35
     */
    int countChild(Long id);

    /**
     * 查询子部门数量
     *
     * @param ids
     * @return int
     * @author RickyWang
     * @date 18/1/1 15:08:53
     */
    int countChild(Long[] ids);
}
