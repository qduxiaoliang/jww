package com.jww.base.am.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.service.SysDeptService;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.dao.mapper.SysDeptMapper;
import com.jww.base.am.dao.mapper.SysTreeMapper;
import com.jww.base.am.model.entity.SysDeptEntity;
import com.jww.base.am.model.entity.SysTreeEntity;
import com.jww.common.core.annotation.DistributedLock;
import com.jww.common.core.base.BaseServiceImpl;
import com.jww.common.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author: Ricky Wang
 * @Date: 17/12/1 14:49:30
 */
@Slf4j
@Service("sysDeptService")
@CacheConfig(cacheNames = AmConstants.AmCacheName.DEPT)
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptMapper, SysDeptEntity> implements SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysTreeMapper sysTreeMapper;

    @Override
    @CachePut
    @DistributedLock(value = "#sysDeptModel.getParentId()")
    public SysDeptEntity addDept(SysDeptEntity sysDeptEntity) {
        QueryWrapper<SysDeptEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", sysDeptEntity.getParentId());
        wrapper.eq("dept_name", sysDeptEntity.getDeptName());
        wrapper.eq("is_del", 0);
        List<SysDeptEntity> deptModelList = sysDeptMapper.selectList(wrapper);
        if (ObjectUtil.isNotNull(super.getOne(wrapper))) {
            throw new BusinessException("同级部门名称不能重复");
        }
        sysDeptEntity.setUnitId(Long.valueOf(1));
        Date now = new Date();
        sysDeptEntity.setCreateTime(now);
        sysDeptEntity.setUpdateTime(now);
        if (sysDeptEntity.getParentId() == null) {
            sysDeptEntity.setParentId(0L);
        }
        return super.add(sysDeptEntity);
    }

    @Override
    @CacheEvict(value = AmConstants.AmCacheName.DEPT, allEntries = true)
    public SysDeptEntity modifyById(SysDeptEntity sysDeptEntity) {
        return super.modifyById(sysDeptEntity);
    }

    @Override
    public IPage<SysDeptEntity> queryListPage(IPage<SysDeptEntity> page) {
        String deptName = page.condition() == null ? null : page.condition().get("dept_name").toString();
        List<SysDeptEntity> list = sysDeptMapper.selectPage(page, deptName);
        page.setRecords(list);
        return page;
    }

    @Override
    @Cacheable
    public SysDeptEntity queryOne(Long id) {
        return sysDeptMapper.selectOne(id);
    }

    @Override
    @CacheEvict(value = AmConstants.AmCacheName.DEPT, allEntries = true)
    public Integer deleteBatch(Long[] ids) {
        int succ = 0;
        for (Long id : ids) {
            Boolean res = false;
            try {
                res = this.delDept(id);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("删除部门失败，id:{}", id, e);
            }
            if (res) {
                succ++;
            }
            log.debug("删除部门完成，id:{}，执行结果：{}", id, res);
        }
        return succ;
    }

    @Override
    public List<SysTreeEntity> queryTree() {
        return this.queryTree(null);
    }

    ;

    @Override
    @Cacheable
    public List<SysTreeEntity> queryTree(Long id) {
        List<SysTreeEntity> sysTreeEntityList = sysTreeMapper.selectDeptTree(id);
        return SysTreeEntity.getTree(sysTreeEntityList);
    }

    @Override
    @DistributedLock
    @CacheEvict(value = AmConstants.AmCacheName.DEPT, allEntries = true)
    public boolean delDept(Long id) {
        int subDeptCount = querySubDeptCount(id);
        if (subDeptCount > 0) {
            throw new BusinessException("必须先删除子部门");
        }
        boolean result = false;
        SysDeptEntity sysDeptEntity = new SysDeptEntity();
        sysDeptEntity.setId(id);
        sysDeptEntity.setIsDel(1);
        sysDeptEntity = super.modifyById(sysDeptEntity);
        if (sysDeptEntity != null) {
            result = true;
        }
        return result;
    }

    @Override
    @Cacheable
    public List<SysDeptEntity> querySubDept(Long id) {
        Assert.notNull(id);
        QueryWrapper<SysDeptEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        wrapper.eq("is_del", 0);
        return sysDeptMapper.selectList(wrapper);
    }

    @Override
    public int querySubDeptCount(Long id) {
        Assert.notNull(id);
        QueryWrapper<SysDeptEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        wrapper.eq("is_del", 0);
        return sysDeptMapper.selectCount(wrapper);
    }

    @Override
    public int querySubDeptCount(Long[] ids) {
        Assert.notNull(ids);
        QueryWrapper<SysDeptEntity> wrapper = new QueryWrapper<>();
        wrapper.in("parent_id", ids);
        wrapper.eq("is_del", 0);
        return sysDeptMapper.selectCount(wrapper);
    }
}
