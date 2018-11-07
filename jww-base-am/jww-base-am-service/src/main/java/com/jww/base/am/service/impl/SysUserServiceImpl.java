package com.jww.base.am.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.dao.mapper.SysRoleMapper;
import com.jww.base.am.dao.mapper.SysUserMapper;
import com.jww.base.am.dao.mapper.SysUserRoleMapper;
import com.jww.base.am.model.dto.SysUserDTO;
import com.jww.base.am.model.entity.SysRoleEntity;
import com.jww.base.am.model.entity.SysUserEntity;
import com.jww.base.am.model.entity.SysUserRoleEntity;
import com.jww.base.am.service.SysUserService;
import com.jww.common.core.annotation.DistributedLock;
import com.jww.common.core.base.BaseServiceImpl;
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
    public SysUserEntity queryByUsername(String username) {
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setUserName(username);
        sysUserEntity.setIsEnable(1);
        QueryWrapper<SysUserEntity> entityWrapper = new QueryWrapper<>(sysUserEntity);
        return super.getOne(entityWrapper);
    }

    @Override
    public IPage<SysUserEntity> queryListPage(IPage<SysUserEntity> page) {
        String searchKey = page.condition() == null ? null : page.condition().get("searchKey").toString();
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
            QueryWrapper<SysUserRoleEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", sysUserEntity.getId());
            sysUserRoleMapper.update(sysUserRoleEntity, wrapper);
        }
        return super.updateBatchById(sysUserEntityList);
    }

    @Override
    public List<SysRoleEntity> queryRoles(Long deptId) {
        Assert.notNull(deptId);
        QueryWrapper<SysRoleEntity> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq("dept_id", deptId);
        return sysRoleMapper.selectList(entityWrapper);
    }

    @DistributedLock
    @CacheEvict(value = AmConstants.AmCacheName.USER, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public SysUserEntity add(SysUserDTO sysUserDTO) {
        sysUserDTO.setCreateTime(new Date());
        if (super.save(sysUserDTO)) {
            addUserRole(sysUserDTO);
            return sysUserDTO;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = AmConstants.AmCacheName.USER, allEntries = true)
    public boolean modify(SysUserDTO sysUserDTO) {
        SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
        sysUserRoleEntity.setUserId(sysUserDTO.getId());
        QueryWrapper<SysUserRoleEntity> queryWrapper = new QueryWrapper<>();
        sysUserRoleMapper.delete(queryWrapper);
        addUserRole(sysUserDTO);
        return super.updateById(sysUserDTO);
    }

    @Override
    @Cacheable
    public List<SysUserEntity> queryRunasList() {
        SysUserEntity userModel = new SysUserEntity();
        userModel.setIsDel(0);
        userModel.setIsEnable(1);
        QueryWrapper<SysUserEntity> wrapper = new QueryWrapper<>();
        // wrapper.setSqlSelect("id_", "user_name", "account_");
        return sysUserMapper.selectList(wrapper);
    }

    @Override
    public List<SysUserRoleEntity> queryUserRoles(Long userId) {
        Assert.notNull(userId);
        QueryWrapper<SysUserRoleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return sysUserRoleMapper.selectList(wrapper);
    }

    private void addUserRole(SysUserDTO sysUserDTO) {
        if (sysUserDTO.getRoles() != null && sysUserDTO.getRoles().length != 0) {
            for (Long roleId : sysUserDTO.getRoles()) {
                SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
                sysUserRoleEntity.setUserId(sysUserDTO.getId());
                sysUserRoleEntity.setCreateTime(new Date());
                sysUserRoleEntity.setUpdateTime(new Date());
                sysUserRoleEntity.setCreateBy(sysUserDTO.getCreateBy());
                sysUserRoleEntity.setUpdateBy(sysUserDTO.getCreateBy());
                sysUserRoleEntity.setRoleId(roleId);
                sysUserRoleMapper.insert(sysUserRoleEntity);
            }
        }
    }

    @Override
    public List<SysUserEntity> queryList() {
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setIsDel(0);
        sysUserEntity.setIsEnable(1);
        QueryWrapper<SysUserEntity> entityWrapper = new QueryWrapper<>(sysUserEntity);
        return super.list(entityWrapper);
    }
}
