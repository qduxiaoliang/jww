package com.jww.base.am.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.dos.SysDeptDO;
import com.jww.common.core.model.dto.TreeDTO;
import com.jww.common.core.base.BaseService;

import java.util.List;

/**
 * 部门管理服务接口
 *
 * @author Ricky Wang
 * @date 2017-12-27 12:02
 */
public interface SysDeptService extends BaseService<SysDeptDO> {

    /**
     * 新增部门
     *
     * @param sysDeptDO
     * @return com.jww.ump.SysDeptModel
     * @author RickyWang
     * @date 18/1/1 15:04:47
     */
    SysDeptDO addDept(SysDeptDO sysDeptDO);

    /**
     * 修改部门
     *
     * @param sysDeptDO
     * @return boolean
     * @author RickyWang
     * @date 18/1/1 15:05:10
     */
    SysDeptDO modifyById(SysDeptDO sysDeptDO);

    /**
     * 分页查询部门
     *
     * @param page
     * @return com.baomidou.mybatisplus.plugins.Page<com.jww.ump.SysDeptModel>
     * @author RickyWang
     * @date 18/1/1 15:05:30
     */
    IPage<SysDeptDO> queryListPage(IPage<SysDeptDO> page);

    /**
     * 查询单个部门
     *
     * @param id
     * @return com.jww.ump.SysDeptModel
     * @author RickyWang
     * @date 18/1/1 15:05:53
     */
    public SysDeptDO queryOne(Long id);

    /**
     * 批量删除部门
     *
     * @param ids
     * @return java.lang.Integer
     * @author RickyWang
     * @date 18/1/1 15:06:11
     */
    public Integer deleteBatch(Long[] ids);

    /**
     * 查询部门数
     *
     * @return java.util.List<com.jww.ump.SysTreeModel>
     * @author RickyWang
     * @date 18/1/1 15:06:33
     */
    public List<TreeDTO> queryTree();

    /**
     * 查询部门数，过滤该id下所有部门
     *
     * @param id
     * @return java.util.List<com.jww.ump.SysTreeModel>
     * @author RickyWang
     * @date 18/1/1 15:06:33
     */
    public List<TreeDTO> queryTree(Long id);

    /**
     * 删除部门
     *
     * @param id
     * @return boolean
     * @author RickyWang
     * @date 18/1/1 15:07:49
     */
    public boolean delDept(Long id);

    /**
     * 按id查询子部门
     *
     * @param id
     * @return java.util.List<com.jww.ump.SysDeptModel>
     * @author RickyWang
     * @date 18/1/1 15:08:09
     */
    public List<SysDeptDO> querySubDept(Long id);

    /**
     * 查询子部门数量
     *
     * @param id
     * @return int
     * @author RickyWang
     * @date 18/1/1 15:08:35
     */
    public int querySubDeptCount(Long id);

    /**
     * 查询子部门数量
     *
     * @param ids
     * @return int
     * @author RickyWang
     * @date 18/1/1 15:08:53
     */
    public int querySubDeptCount(Long[] ids);
}
