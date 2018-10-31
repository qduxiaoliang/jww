package com.jww.base.am.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jww.base.am.api.SysRoleMenuService;
import com.jww.base.am.api.SysRoleService;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.dao.mapper.SysRoleMapper;
import com.jww.base.am.dao.mapper.SysRoleMenuMapper;
import com.jww.base.am.model.entity.SysRoleMenuEntity;
import com.jww.base.am.model.entity.SysRoleEntity;
import com.jww.common.core.base.BaseServiceImpl;
import com.jww.common.core.exception.BusinessException;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.StrUtil;
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
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRoleEntity> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public Page<SysRoleEntity> queryListPage(Page<SysRoleEntity> page) {
        SysRoleEntity sysRoleEntity = new SysRoleEntity();
        sysRoleEntity.setIsDel(0);
        EntityWrapper<SysRoleEntity> entityWrapper = new EntityWrapper<>(sysRoleEntity);
        if (ObjectUtil.isNotNull(page.getCondition())) {
            StringBuilder conditionSql = new StringBuilder();
            Map<String, Object> paramMap = page.getCondition();
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
            entityWrapper.and(StrUtil.removeSuffix(conditionSql.toString(), "AND "));
        }
        page.setCondition(null);

        return page.setRecords(sysRoleMapper.selectRoleList(page, entityWrapper));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = AmConstants.AmCacheName.ROLE, allEntries = true)
    public SysRoleEntity add(SysRoleEntity sysRoleEntity) {
        // 根据角色名称和部门检查是否存在相同的角色
        SysRoleEntity checkModel = new SysRoleEntity();
        checkModel.setIsDel(0);
        checkModel.setRoleName(sysRoleEntity.getRoleName());
        checkModel.setDeptId(sysRoleEntity.getDeptId());
        EntityWrapper<SysRoleEntity> entityWrapper = new EntityWrapper<>(checkModel);
        if (ObjectUtil.isNotNull(super.selectOne(entityWrapper))) {
            throw new BusinessException("已存在相同名称的角色");
        }
        SysRoleEntity result = super.add(sysRoleEntity);
        // 这里增加CollectionUtil.isNotEmpty(sysRoleModel.getMenuIdList())判断是由于新增角色时允许不选择权限
        if (result != null && CollectionUtil.isNotEmpty(sysRoleEntity.getMenuIdList())) {
            sysRoleMenuService.insertBatch(getRoleMenuListByMenuIds(sysRoleEntity, sysRoleEntity.getMenuIdList()));
        }
        return result;
    }


    @Override
    @CacheEvict(value = AmConstants.AmCacheName.ROLE, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public SysRoleEntity modifyById(SysRoleEntity sysRoleEntity) {
        SysRoleEntity result = super.modifyById(sysRoleEntity);
        // 这里增加CollectionUtil.isNotEmpty(sysRoleModel.getMenuIdList())判断是由于删除角色时实际会调用modifyById方法去更新is_del字段，只有当修改角色时menuIdList才不会为空
        if (CollectionUtil.isNotEmpty(sysRoleEntity.getMenuIdList())) {
            SysRoleMenuEntity sysRoleMenuEntity = new SysRoleMenuEntity();
            sysRoleMenuEntity.setRoleId(sysRoleEntity.getId());
            EntityWrapper<SysRoleMenuEntity> entityWrapper = new EntityWrapper<>(sysRoleMenuEntity);
            // 关系数据相对不重要，直接数据库删除
            sysRoleMenuService.delete(entityWrapper);
            sysRoleMenuService.insertBatch(getRoleMenuListByMenuIds(sysRoleEntity, sysRoleEntity.getMenuIdList()));
        }
        return result;
    }

    /**
     * 根据角色实体和角色对应的菜单ID集合获取角色菜单实体集合
     *
     * @param sysRoleEntity
     * @param menuIds
     * @return
     * @author wanyong
     * @date 2017-12-24 14:49
     */
    private List<SysRoleMenuEntity> getRoleMenuListByMenuIds(SysRoleEntity sysRoleEntity, List<Long> menuIds) {
        List<SysRoleMenuEntity> sysRoleMenuEntityList = new ArrayList<>(5);
        for (Long menuId : menuIds) {
            SysRoleMenuEntity sysRoleMenuEntity = new SysRoleMenuEntity();
            sysRoleMenuEntity.setRoleId(sysRoleEntity.getId());
            sysRoleMenuEntity.setMenuId(menuId);
            sysRoleMenuEntity.setCreateBy(sysRoleEntity.getUpdateBy());
            sysRoleMenuEntity.setUpdateBy(sysRoleEntity.getUpdateBy());
            sysRoleMenuEntityList.add(sysRoleMenuEntity);
        }
        return sysRoleMenuEntityList;
    }

    @Override
    @Cacheable
    public List<SysRoleEntity> queryRoles(Long deptId) {
        Assert.notNull(deptId);
        EntityWrapper<SysRoleEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("dept_id", deptId);
        return sysRoleMapper.selectList(entityWrapper);
    }

    @Override
    @CacheEvict(value = AmConstants.AmCacheName.ROLE, allEntries = true)
    public boolean deleteBatchIds(List<? extends Serializable> idList) {
        List<SysRoleEntity> roleModelList = new ArrayList<SysRoleEntity>();
        idList.forEach(id -> {
            SysRoleEntity entity = new SysRoleEntity();
            entity.setId((Long) id);
            entity.setIsDel(1);
            entity.setUpdateTime(new Date());
            roleModelList.add(entity);
        });
        return super.updateBatchById(roleModelList);
    }
}
