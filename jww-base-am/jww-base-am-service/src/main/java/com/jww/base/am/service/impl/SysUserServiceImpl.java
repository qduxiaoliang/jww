package com.jww.base.am.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jww.base.am.api.SysUserService;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.dao.mapper.SysRoleMapper;
import com.jww.base.am.dao.mapper.SysUserMapper;
import com.jww.base.am.dao.mapper.SysUserRoleMapper;
import com.jww.base.am.model.entity.SysRoleEntity;
import com.jww.base.am.model.entity.SysUserEntity;
import com.jww.base.am.model.entity.SysUserRoleEntity;
import com.jww.common.core.annotation.DistributedLock;
import com.jww.common.core.base.BaseServiceImpl;
import com.xiaoleilu.hutool.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户管理服务实现
 *
 * @author wanyong
 * @date 2017/11/17 16:43
 */
@Slf4j
@Service("sysUserService")
@CacheConfig(cacheNames = AmConstants.AmCacheName.USER)
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public SysUserEntity queryByAccount(String account) {
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setAccount(account);
        sysUserEntity.setEnable(1);
        EntityWrapper<SysUserEntity> entityWrapper = new EntityWrapper<>(sysUserEntity);
        return super.selectOne(entityWrapper);
    }

    @Override
    public Page<SysUserEntity> queryListPage(Page<SysUserEntity> page) {
        String searchKey = page.getCondition() == null ? null : page.getCondition().get("searchKey").toString();
        List<SysUserEntity> list = sysUserMapper.selectPage(page, searchKey);
        page.setRecords(list);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = AmConstants.AmCacheName.USER, allEntries = true)
    public boolean delBatchByIds(List<Long> ids) {
        List<SysUserEntity> sysUserEntityList = new ArrayList<>(5);
        for (Long id : ids) {
            SysUserEntity sysUserEntity = new SysUserEntity();
            sysUserEntity.setId(id);
            sysUserEntity.setIsDel(1);

            sysUserEntityList.add(sysUserEntity);

            SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
            sysUserRoleEntity.setIsDel(1);
            sysUserRoleEntity.setUpdateTime(new Date());
            sysUserRoleEntity.setUpdateBy(sysUserEntity.getCreateBy());
            EntityWrapper<SysUserRoleEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("user_id", sysUserEntity.getId());
            sysUserRoleMapper.update(sysUserRoleEntity, wrapper);
        }
        return super.updateBatchById(sysUserEntityList);
    }

    @Override
    public List<SysRoleEntity> queryRoles(Long deptId) {
        Assert.notNull(deptId);
        EntityWrapper<SysRoleEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("dept_id", deptId);
        return sysRoleMapper.selectList(entityWrapper);
    }

    @Override
    @CacheEvict(value = AmConstants.AmCacheName.USER, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock
    public SysUserEntity add(SysUserEntity sysUserEntity) {
        sysUserEntity.setCreateTime(new Date());
        sysUserEntity.setUpdateTime(new Date());
        if (super.insert(sysUserEntity)) {
            insertUserRole(sysUserEntity);
            return sysUserEntity;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = AmConstants.AmCacheName.USER, allEntries = true)
    public boolean modifyUser(SysUserEntity sysUserEntity) {
        boolean result = false;
        EntityWrapper<SysUserRoleEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", sysUserEntity.getId());
        sysUserRoleMapper.delete(wrapper);
        insertUserRole(sysUserEntity);
        return super.updateById(sysUserEntity);
    }

    @Override
    @Cacheable
    public List<SysUserEntity> queryRunasList() {
        SysUserEntity userModel = new SysUserEntity();
        userModel.setIsDel(0);
        userModel.setEnable(1);
        EntityWrapper<SysUserEntity> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("id_", "user_name", "account_");
        return sysUserMapper.selectList(wrapper);
    }

    @Override
    public List<SysUserRoleEntity> queryUserRoles(Long userId) {
        Assert.notNull(userId);
        EntityWrapper<SysUserRoleEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId);
        return sysUserRoleMapper.selectList(wrapper);
    }

    @Override
    public SysUserEntity queryOne(Long id) {
        SysUserEntity sysUserEntity = sysUserMapper.selectOne(id);
        return sysUserEntity;
    }

    private void insertUserRole(SysUserEntity sysUserEntity) {
        if (sysUserEntity.getRole() != null && sysUserEntity.getRole().length != 0) {
            for (Long roleId : sysUserEntity.getRole()) {
                SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
                sysUserRoleEntity.setUserId(sysUserEntity.getId());
                sysUserRoleEntity.setCreateTime(new Date());
                sysUserRoleEntity.setUpdateTime(new Date());
                sysUserRoleEntity.setCreateBy(sysUserEntity.getCreateBy());
                sysUserRoleEntity.setUpdateBy(sysUserEntity.getCreateBy());
                sysUserRoleEntity.setRoleId(roleId);
                sysUserRoleMapper.insert(sysUserRoleEntity);
            }
        }
    }

    @Override
    public List<SysUserEntity> queryList() {
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setIsDel(0);
        sysUserEntity.setEnable(1);
        EntityWrapper<SysUserEntity> entityWrapper = new EntityWrapper<>(sysUserEntity);
        return super.selectList(entityWrapper);
    }
}
