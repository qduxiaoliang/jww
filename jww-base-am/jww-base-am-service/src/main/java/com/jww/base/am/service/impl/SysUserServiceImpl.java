package com.jww.base.am.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.dao.mapper.SysRoleMapper;
import com.jww.base.am.dao.mapper.SysUserMapper;
import com.jww.base.am.dao.mapper.SysUserRoleMapper;
import com.jww.base.am.model.dto.SysRoleDTO;
import com.jww.base.am.model.dto.SysUserDTO;
import com.jww.base.am.model.dto.SysUserRoleDTO;
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
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUserDTO> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public SysUserDTO getByUsername(String username) {
        SysUserDTO sysUserDTO = new SysUserDTO();
        sysUserDTO.setUsername(username);
        sysUserDTO.setIsEnable(1);
        QueryWrapper<SysUserDTO> entityWrapper = new QueryWrapper<>(sysUserDTO);
        return super.getOne(entityWrapper);
    }

    @Override
    public IPage<SysUserDTO> listPage(IPage<SysUserDTO> page) {
        String searchKey = page.condition() == null ? null : page.condition().get("searchKey").toString();
        List<SysUserDTO> list = sysUserMapper.selectPage(page, searchKey);
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
    public List<SysRoleDTO> listRole(Long deptId) {
        Assert.notNull(deptId);
        QueryWrapper<SysRoleDTO> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq("dept_id", deptId);
        return sysRoleMapper.selectList(entityWrapper);
    }

    @DistributedLock
    @CacheEvict(value = AmConstants.AmCacheName.USER, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public SysUserDTO add(SysUserDTO sysUserDTO) {
        sysUserDTO.setCreateTime(new Date());
        if (super.save(sysUserDTO)) {
            saveUserRole(sysUserDTO);
            return sysUserDTO;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = AmConstants.AmCacheName.USER, allEntries = true)
    public boolean updateById(SysUserDTO sysUserDTO) {
        SysUserRoleDTO sysUserRoleDTO = new SysUserRoleDTO();
        sysUserRoleDTO.setUserId(sysUserDTO.getId());
        QueryWrapper<SysUserRoleDTO> queryWrapper = new QueryWrapper<>(sysUserRoleDTO);
        sysUserRoleMapper.delete(queryWrapper);
        saveUserRole(sysUserDTO);
        return super.updateById(sysUserDTO);
    }

    @Override
    @Cacheable
    public List<SysUserDTO> listRunas() {
        SysUserDTO sysUserDTO = new SysUserDTO();
        sysUserDTO.setIsDel(0);
        sysUserDTO.setIsEnable(1);
        QueryWrapper<SysUserDTO> queryWrapper = new QueryWrapper<>(sysUserDTO);
        // wrapper.setSqlSelect("id_", "user_name", "account_");
        return sysUserMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysUserRoleDTO> listUserRole(Long userId) {
        Assert.notNull(userId);
        QueryWrapper<SysUserRoleDTO> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return sysUserRoleMapper.selectList(wrapper);
    }

    /**
     * 保存用户角色
     *
     * @param sysUserDTO 用户传输实体
     * @author wanyong
     * @date 2018-11-8 10:03
     */
    private void saveUserRole(SysUserDTO sysUserDTO) {
        if (sysUserDTO.getRoleIds() != null && sysUserDTO.getRoleIds().length != 0) {
            for (Long roleId : sysUserDTO.getRoleIds()) {
                SysUserRoleDTO sysUserRoleDTO = new SysUserRoleDTO();
                sysUserRoleDTO.setUserId(sysUserDTO.getId());
                sysUserRoleDTO.setCreateTime(new Date());
                sysUserRoleDTO.setUpdateTime(new Date());
                sysUserRoleDTO.setCreateBy(sysUserDTO.getCreateBy());
                sysUserRoleDTO.setUpdateBy(sysUserDTO.getCreateBy());
                sysUserRoleDTO.setRoleId(roleId);
                sysUserRoleMapper.insert(sysUserRoleDTO);
            }
        }
    }
}
