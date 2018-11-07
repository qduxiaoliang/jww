package com.jww.base.am.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.dao.mapper.SysRoleMapper;
import com.jww.base.am.dao.mapper.SysRoleMenuMapper;
import com.jww.base.am.model.dto.SysRoleDTO;
import com.jww.base.am.model.dos.SysRoleDO;
import com.jww.base.am.model.dos.SysRoleMenuEntity;
import com.jww.base.am.service.SysRoleMenuService;
import com.jww.base.am.service.SysRoleService;
import com.jww.common.core.base.BaseServiceImpl;
import com.jww.common.core.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author wanyong
 * @since 2017-12-17
 */
@Service("sysRoleService")
@CacheConfig(cacheNames = AmConstants.AmCacheName.ROLE)
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRoleDO> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public IPage<SysRoleDO> listPage(IPage<SysRoleDO> page) {
        SysRoleDO sysRoleDO = new SysRoleDO();
        sysRoleDO.setIsDel(0);
        QueryWrapper<SysRoleDO> entityWrapper = new QueryWrapper<>(sysRoleDO);
        if (ObjectUtil.isNotNull(page.condition())) {
            StringBuilder conditionSql = new StringBuilder();
            Map<Object, Object> paramMap = page.condition();
            String deptId = "dept_id";
            paramMap.forEach((k, v) -> {
                if (StrUtil.isNotBlank(v + "")) {
                    if (deptId.equals(k)) {
                        conditionSql.append(k + " = " + v + " AND ");
                    } else {
                        conditionSql.append(k + " like '%" + v + "%' AND ");
                    }
                }
            });
            // entityWrapper.and(StrUtil.removeSuffix(conditionSql.toString(), "AND "));
        }
        // page.setCondition(null);
        return page.setRecords(sysRoleMapper.selectRoleList(page, entityWrapper));
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = AmConstants.AmCacheName.ROLE, allEntries = true)
    public SysRoleDTO add(SysRoleDTO sysRoleDTO) {
        // 1、根据角色名称和部门检查是否存在相同的角色
        SysRoleDTO checkSysRoleDTO = new SysRoleDTO();
        checkSysRoleDTO.setRoleName(sysRoleDTO.getRoleName());
        QueryWrapper<SysRoleDO> queryWrapper = new QueryWrapper<>(checkSysRoleDTO);
        if (ObjectUtil.isNotNull(super.getOne(queryWrapper))) {
            throw new BusinessException("已存在相同名称的角色");
        }
        super.save(sysRoleDTO);
        return sysRoleDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = AmConstants.AmCacheName.ROLE, allEntries = true)
    public SysRoleDTO modifyById(SysRoleDTO sysRoleDTO) {
        SysRoleDO result = super.modifyById(sysRoleDTO);
        // 这里增加CollectionUtil.isNotEmpty(sysRoleModel.getMenuIdList())判断是由于删除角色时实际会调用modifyById方法去更新is_del字段，只有当修改角色时menuIdList才不会为空
        if (CollectionUtil.isNotEmpty(sysRoleEntity.getMenuIdList())) {
            SysRoleMenuEntity sysRoleMenuEntity = new SysRoleMenuEntity();
            sysRoleMenuEntity.setRoleId(sysRoleEntity.getId());
            QueryWrapper<SysRoleMenuEntity> entityWrapper = new QueryWrapper<>(sysRoleMenuEntity);
            // 关系数据相对不重要，直接数据库删除
            sysRoleMenuService.remove(entityWrapper);
            sysRoleMenuService.saveBatch(getRoleMenuListByMenuIds(sysRoleEntity, sysRoleEntity.getMenuIdList()));
        }
        return result;
    }

    /**
     * 根据角色实体和角色对应的菜单ID集合获取角色菜单实体集合
     *
     * @param sysRoleDO
     * @param menuIds
     * @return
     * @author wanyong
     * @date 2017-12-24 14:49
     */
    private List<SysRoleMenuEntity> getRoleMenuListByMenuIds(SysRoleDO sysRoleDO, List<Long> menuIds) {
        List<SysRoleMenuEntity> sysRoleMenuEntityList = new ArrayList<>(5);
        for (Long menuId : menuIds) {
            SysRoleMenuEntity sysRoleMenuEntity = new SysRoleMenuEntity();
            sysRoleMenuEntity.setRoleId(sysRoleDO.getId());
            sysRoleMenuEntity.setMenuId(menuId);
            sysRoleMenuEntity.setCreateBy(sysRoleDO.getUpdateBy());
            sysRoleMenuEntity.setUpdateBy(sysRoleDO.getUpdateBy());
            sysRoleMenuEntityList.add(sysRoleMenuEntity);
        }
        return sysRoleMenuEntityList;
    }

    @Override
    @Cacheable
    public List<SysRoleDO> queryRoles(Long deptId) {
        Assert.notNull(deptId);
        QueryWrapper<SysRoleDO> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq("dept_id", deptId);
        return sysRoleMapper.selectList(entityWrapper);
    }

    @CacheEvict(value = AmConstants.AmCacheName.ROLE, allEntries = true)
    public boolean removeByIds(List<? extends Serializable> idList) {
        List<SysRoleDO> roleModelList = new ArrayList<SysRoleDO>();
        idList.forEach(id -> {
            SysRoleDO entity = new SysRoleDO();
            entity.setId((Long) id);
            entity.setIsDel(1);
            entity.setUpdateTime(new Date());
            roleModelList.add(entity);
        });
        return super.updateBatchById(roleModelList);
    }


}
