package com.jww.base.am.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.dao.mapper.SysDeptMapper;
import com.jww.base.am.dao.mapper.SysTreeMapper;
import com.jww.base.am.model.dos.SysDeptDO;
import com.jww.base.am.service.SysDeptService;
import com.jww.common.core.annotation.DistributedLock;
import com.jww.common.core.base.BaseServiceImpl;
import com.jww.common.core.exception.BusinessException;
import com.jww.common.core.model.dto.TreeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门管理实现类
 *
 * @author wanyong
 * @date 2018-11-8 16:02
 */
@Slf4j
@Service("sysDeptService")
@CacheConfig(cacheNames = AmConstants.AmCacheName.DEPT)
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptMapper, SysDeptDO> implements SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysTreeMapper sysTreeMapper;

    @Override
    @CacheEvict(value = AmConstants.AmCacheName.DEPT, allEntries = true)
    public boolean save(SysDeptDO sysDeptDO) {
        SysDeptDO checkSysDeptDO = new SysDeptDO();
        checkSysDeptDO.setParentId(sysDeptDO.getParentId());
        checkSysDeptDO.setDeptName(sysDeptDO.getDeptName());
        QueryWrapper<SysDeptDO> queryWrapper = new QueryWrapper<>(checkSysDeptDO);
        if (super.count(queryWrapper) > 0) {
            throw new BusinessException("同级部门名称不能重复");
        }
        if (sysDeptDO.getParentId() == null) {
            sysDeptDO.setParentId(0L);
        }
        return super.save(sysDeptDO);
    }

    @Override
    @CacheEvict(value = AmConstants.AmCacheName.DEPT, allEntries = true)
    public boolean updateById(SysDeptDO sysDeptDO) {
        return super.updateById(sysDeptDO);
    }

    @Override
    public IPage<SysDeptDO> listPage(IPage<SysDeptDO> page) {
        String deptName = page.condition() == null ? null : page.condition().get("dept_name").toString();
        List<SysDeptDO> list = sysDeptMapper.selectPage(page, deptName);
        page.setRecords(list);
        return page;
    }

    @Override
    @Cacheable
    public SysDeptDO getOne(Long deptId) {
        SysDeptDO sysDeptDO = new SysDeptDO();
        sysDeptDO.setId(deptId);
        QueryWrapper<SysDeptDO> queryWrapper = new QueryWrapper<>(sysDeptDO);
        return super.getOne(queryWrapper);
    }

    @Override
    @CacheEvict(value = AmConstants.AmCacheName.DEPT, allEntries = true)
    public boolean removeByIds(List<Long> ids) {
        return super.removeByIds(ids);
    }

    @Override
    public List<TreeDTO> listTree() {
        return this.listTree(null);
    }

    @Cacheable
    public List<TreeDTO> listTree(Long id) {
        List<TreeDTO> treeDTOList = sysTreeMapper.selectDeptTree(id);
        return TreeDTO.getTree(treeDTOList);
    }

    @Override
    public List<TreeDTO> ListTreeExcludeId(Long id) {
        return null;
    }

    @Override
    @DistributedLock
    @CacheEvict(value = AmConstants.AmCacheName.DEPT, allEntries = true)
    public boolean removeById(Long deptId) {
        int subDeptCount = countChild(deptId);
        if (subDeptCount > 0) {
            throw new BusinessException("必须先删除子部门");
        }
        return super.removeById(deptId);
    }

    @Override
    @Cacheable
    public List<SysDeptDO> listChild(Long id) {
        Assert.notNull(id);
        SysDeptDO sysDeptDO = new SysDeptDO();
        sysDeptDO.setParentId(id);
        QueryWrapper<SysDeptDO> queryWrapper = new QueryWrapper<>(sysDeptDO);
        return super.list(queryWrapper);
    }

    @Override
    public int countChild(Long id) {
        Assert.notNull(id);
        SysDeptDO sysDeptDO = new SysDeptDO();
        sysDeptDO.setParentId(id);
        QueryWrapper<SysDeptDO> queryWrapper = new QueryWrapper<>(sysDeptDO);
        return super.count(queryWrapper);
    }

    @Override
    public int countChild(Long[] ids) {
        Assert.notNull(ids);
        QueryWrapper<SysDeptDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("parent_id", ids);
        return super.count(queryWrapper);
    }
}
