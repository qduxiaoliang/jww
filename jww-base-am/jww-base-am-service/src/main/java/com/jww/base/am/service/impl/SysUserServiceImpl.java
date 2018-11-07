package com.jww.base.am.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.dao.mapper.SysRoleMapper;
import com.jww.base.am.dao.mapper.SysUserMapper;
import com.jww.base.am.dao.mapper.SysUserRoleMapper;
import com.jww.base.am.model.dos.SysRoleDO;
import com.jww.base.am.model.dos.SysUserDO;
import com.jww.base.am.model.dos.SysUserRoleDO;
import com.jww.base.am.model.dto.SysUserDTO;
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
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUserDTO> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public SysUserDO queryByUsername(String username) {
        SysUserDO sysUserDO = new SysUserDO();
        sysUserDO.setUserName(username);
        sysUserDO.setIsEnable(1);
        QueryWrapper<SysUserDO> entityWrapper = new QueryWrapper<>(sysUserDO);
        return super.getOne(entityWrapper);
    }

    @Override
    public IPage<SysUserDO> queryListPage(IPage<SysUserDO> page) {
        String searchKey = page.condition() == null ? null : page.condition().get("searchKey").toString();
        List<SysUserDO> list = sysUserMapper.selectPage(page, searchKey);
        page.setRecords(list);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = AmConstants.AmCacheName.USER, allEntries = true)
    public boolean delBatchByIds(List<Long> ids) {
        List<SysUserDO> sysUserDOList = new ArrayList<>(5);
        for (Long id : ids) {
            SysUserDO sysUserDO = new SysUserDO();
            sysUserDO.setId(id);
            sysUserDO.setIsDel(1);

            sysUserDOList.add(sysUserDO);

            SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
            sysUserRoleDO.setIsDel(1);
            sysUserRoleDO.setUpdateTime(new Date());
            sysUserRoleDO.setUpdateBy(sysUserDO.getCreateBy());
            QueryWrapper<SysUserRoleDO> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", sysUserDO.getId());
            sysUserRoleMapper.update(sysUserRoleDO, wrapper);
        }
        return super.updateBatchById(sysUserDOList);
    }

    @Override
    public List<SysRoleDO> queryRoles(Long deptId) {
        Assert.notNull(deptId);
        QueryWrapper<SysRoleDO> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq("dept_id", deptId);
        return sysRoleMapper.selectList(entityWrapper);
    }

    @DistributedLock
    @CacheEvict(value = AmConstants.AmCacheName.USER, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public SysUserDO add(SysUserDTO sysUserDTO) {
        sysUserDTO.setCreateTime(new Date());
        if (super.save(sysUserDTO)) {
            addUserRole(sysUserDTO);
            return sysUserDTO;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = AmConstants.AmCacheName.USER, allEntries = true)
    public SysUserDTO modifyById(SysUserDTO sysUserDTO) {
        SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
        sysUserRoleDO.setUserId(sysUserDTO.getId());
        QueryWrapper<SysUserRoleDO> queryWrapper = new QueryWrapper<>();
        sysUserRoleMapper.delete(queryWrapper);
        addUserRole(sysUserDTO);
        return super.updateById(sysUserDTO);
    }

    @Override
    @Cacheable
    public List<SysUserDO> queryRunasList() {
        SysUserDO userModel = new SysUserDO();
        userModel.setIsDel(0);
        userModel.setIsEnable(1);
        QueryWrapper<SysUserDO> wrapper = new QueryWrapper<>();
        // wrapper.setSqlSelect("id_", "user_name", "account_");
        return sysUserMapper.selectList(wrapper);
    }

    @Override
    public List<SysUserRoleDO> queryUserRoles(Long userId) {
        Assert.notNull(userId);
        QueryWrapper<SysUserRoleDO> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return sysUserRoleMapper.selectList(wrapper);
    }

    private void addUserRole(SysUserDTO sysUserDTO) {
        if (sysUserDTO.getRoleIds() != null && sysUserDTO.getRoleIds().length != 0) {
            for (Long roleId : sysUserDTO.getRoleIds()) {
                SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
                sysUserRoleDO.setUserId(sysUserDTO.getId());
                sysUserRoleDO.setCreateTime(new Date());
                sysUserRoleDO.setUpdateTime(new Date());
                sysUserRoleDO.setCreateBy(sysUserDTO.getCreateBy());
                sysUserRoleDO.setUpdateBy(sysUserDTO.getCreateBy());
                sysUserRoleDO.setRoleId(roleId);
                sysUserRoleMapper.insert(sysUserRoleDO);
            }
        }
    }

    @Override
    public List<SysUserDO> queryList() {
        SysUserDO sysUserDO = new SysUserDO();
        sysUserDO.setIsDel(0);
        sysUserDO.setIsEnable(1);
        QueryWrapper<SysUserDO> entityWrapper = new QueryWrapper<>(sysUserDO);
        return super.list(entityWrapper);
    }
}
