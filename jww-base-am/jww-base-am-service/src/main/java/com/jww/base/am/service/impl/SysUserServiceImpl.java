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
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUserDO> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public SysUserDO getByUsername(String username) {
        SysUserDO sysUserDO = new SysUserDO();
        sysUserDO.setUsername(username);
        sysUserDO.setIsEnable(1);
        QueryWrapper<SysUserDO> entityWrapper = new QueryWrapper<>(sysUserDO);
        return super.getOne(entityWrapper);
    }

    @Override
    public IPage<SysUserDO> listPage(IPage<SysUserDO> page) {
        String searchKey = page.condition() == null ? null : page.condition().get("searchKey").toString();
        List<SysUserDO> list = sysUserMapper.selectPage(page, searchKey);
        page.setRecords(list);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = AmConstants.AmCacheName.USER, allEntries = true)
    public boolean removeByIds(List<Long> ids) {
        // 1、删除用户角色关系数据
        sysUserRoleMapper.deleteBatchIds(ids);
        // 2、删除用户数据
        return super.removeByIds(ids);
    }

    @Override
    public List<SysRoleDO> listRole(Long deptId) {
        Assert.notNull(deptId);
        QueryWrapper<SysRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id", deptId);
        return sysRoleMapper.selectList(queryWrapper);
    }

    @Override
    @DistributedLock
    @CacheEvict(value = AmConstants.AmCacheName.USER, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SysUserDO sysUserDO) {
        sysUserDO.setCreateTime(new Date());
        if (super.save(sysUserDO)) {
            saveUserRole(sysUserDO);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = AmConstants.AmCacheName.USER, allEntries = true)
    public boolean updateById(SysUserDO sysUserDO) {
        SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
        sysUserRoleDO.setUserId(sysUserDO.getId());
        QueryWrapper<SysUserRoleDO> queryWrapper = new QueryWrapper<>(sysUserRoleDO);
        sysUserRoleMapper.delete(queryWrapper);
        saveUserRole(sysUserDO);
        return super.updateById(sysUserDO);
    }

    @Override
    @Cacheable
    public List<SysUserDO> listRunas() {
        SysUserDO sysUserDTO = new SysUserDO();
        sysUserDTO.setIsEnable(1);
        QueryWrapper<SysUserDO> queryWrapper = new QueryWrapper<>(sysUserDTO);
        // wrapper.setSqlSelect("id_", "user_name", "account_");
        // return sysUserMapper.selectList(queryWrapper);
        return null;
    }

    @Override
    public List<SysUserRoleDO> listUserRole(Long userId) {
        Assert.notNull(userId);
        QueryWrapper<SysUserRoleDO> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return sysUserRoleMapper.selectList(wrapper);
    }

    /**
     * 保存用户角色
     *
     * @param sysUserDO 用户传输实体
     * @author wanyong
     * @date 2018-11-8 10:03
     */
    private void saveUserRole(SysUserDO sysUserDO) {
//        if (sysUserDO.getRoleIds() != null && sysUserDO.getRoleIds().length != 0) {
//            for (Long roleId : sysUserDO.getRoleIds()) {
//                SysUserRoleDTO sysUserRoleDTO = new SysUserRoleDTO();
//                sysUserRoleDTO.setUserId(sysUserDO.getId());
//                sysUserRoleDTO.setCreateTime(new Date());
//                sysUserRoleDTO.setUpdateTime(new Date());
//                sysUserRoleDTO.setCreateBy(sysUserDO.getCreateBy());
//                sysUserRoleDTO.setUpdateBy(sysUserDO.getCreateBy());
//                sysUserRoleDTO.setRoleId(roleId);
//                sysUserRoleMapper.insert(sysUserRoleDTO);
//            }
//        }
    }
}
