package com.jww.base.am.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.common.AmConstants.AmCacheName;
import com.jww.base.am.dao.mapper.SysResourceMapper;
import com.jww.base.am.dao.mapper.SysRoleResourceMapper;
import com.jww.base.am.dao.mapper.SysTreeMapper;
import com.jww.base.am.model.dos.SysResourceDO;
import com.jww.base.am.service.SysResourceService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author wanyong
 * @since 2017-11-29
 */
@Slf4j
@Service("sysResourceService")
@CacheConfig(cacheNames = AmCacheName.RESOURCE)
public class SysResourceServiceImpl extends BaseServiceImpl<SysResourceMapper, SysResourceDO> implements SysResourceService {

    @Autowired
    private SysResourceMapper sysResourceMapper;

    @Autowired
    private SysTreeMapper sysTreeMapper;

    @Autowired
    private SysRoleResourceMapper sysRoleResourceMapper;

    @Override
    @Cacheable
    public List<SysResourceDO> list() {
        SysResourceDO sysResourceDO = new SysResourceDO();
        QueryWrapper<SysResourceDO> queryWrapper = new QueryWrapper<>(sysResourceDO);
        queryWrapper.orderByAsc("parent_id,sort_no");
        return super.list(queryWrapper);
    }

    @Override
    public IPage<SysResourceDO> listPage(IPage<SysResourceDO> page) {
        QueryWrapper<SysResourceDO> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(page.condition())) {
            StringBuilder conditionSql = new StringBuilder();
            Map<Object, Object> paramMap = page.condition();
            paramMap.forEach((k, v) -> {
                if (NumberUtil.isNumber(v + "")) {
                    conditionSql.append("a.").append(k + " = " + v + " and ");
                } else if (StrUtil.isNotBlank(v + "")) {
                    conditionSql.append("a.").append(k + " like '%" + v + "%' and ");
                }
            });
            if (StrUtil.isNotBlank(conditionSql)) {
                // wrapper.where(StrUtil.removeSuffix(conditionSql.toString(), "and "));
            }
        }
        // wrapper.orderBy(" parent_id, sort_no ", true);
        // page.setCondition(null);
        return super.page(page, queryWrapper);
    }

    @Override
    @Cacheable
    public List<TreeDTO> listMenuTreeByUserId(Long userId) {
        List<SysResourceDO> sysResourceDOList;
        // 如果是超级管理员，则查询所有目录菜单
        if (AmConstants.USERID_ADMIN.equals(userId)) {
            SysResourceDO sysResourceDO = new SysResourceDO();
            sysResourceDO.setIsEnable(1);
            sysResourceDO.setIsShow(1);
            sysResourceDO.setResourceType(2);
            QueryWrapper<SysResourceDO> queryWrapper = new QueryWrapper<>(sysResourceDO);
            queryWrapper.orderByAsc("parent_id,sort_no");
            sysResourceDOList = sysResourceMapper.selectList(queryWrapper);
        } else {
            sysResourceDOList = sysResourceMapper.selectMenuTreeByUserId(userId);
        }
        return convertTreeData(sysResourceDOList, null);
    }

    @Override
    @Cacheable
    public List<TreeDTO> listFuncMenuTree() {
        List<SysResourceDO> sysResourceDOList = list();
        return convertTreeData(sysResourceDOList, null);
    }

    @Override
    @Cacheable(value = AmCacheName.ROLE)
    public List<TreeDTO> listFuncMenuTree(Long roleId) {
        List<SysResourceDO> sysResourceDOList = list();
        List<Long> menuIdList = sysRoleResourceMapper.selectMenuIdListByRoleId(roleId);
        return convertTreeData(sysResourceDOList, menuIdList.toArray());
    }

    @Override
    @Cacheable
    public List<TreeDTO> listTree(Long id, Integer menuType) {
        List<TreeDTO> treeDTOList = sysTreeMapper.selectMenuTree(id, menuType);
        return TreeDTO.getTree(treeDTOList);
    }

    @Override
    @DistributedLock
    @CacheEvict(value = AmCacheName.RESOURCE, allEntries = true)
    public boolean removeById(Long resourceId) {
        // 查询是否有子菜单，如果有则返回false，否则允许删除
        SysResourceDO sysResourceDO = new SysResourceDO();
        sysResourceDO.setParentId(resourceId);
        QueryWrapper<SysResourceDO> queryWrapper = new QueryWrapper<>(sysResourceDO);
        List<SysResourceDO> childList = super.list(queryWrapper);
        if (CollUtil.isNotEmpty(childList)) {
            log.error("删除菜单[id:{}]失败，请先删除子菜单", childList);
            throw new BusinessException("删除菜单失败，请先删除子菜单");
        }
        return super.removeById(resourceId);
    }

    @Override
    @CacheEvict(value = AmCacheName.RESOURCE, allEntries = true)
    public boolean removeByIds(Long[] resourceIds) {
        return super.removeByIds(null);
    }

    /**
     * 查询所有父级菜单
     *
     * @return List<SysMenuModel>
     * @author shadj
     * @date 2018/1/25 22:37
     */
    @Override
    public List<SysResourceDO> listParentMenu() {
        return sysResourceMapper.selectParentMenu();
    }

    @Override
    @CacheEvict(value = AmCacheName.RESOURCE, allEntries = true)
    @DistributedLock(value = "#sysMenuModel.getParentId()")
    public boolean save(SysResourceDO sysResourceDO) {
        // 名称重复验证，同一目录下，菜单名称不能相同
        SysResourceDO checkSysResourceDO = new SysResourceDO();
        checkSysResourceDO.setParentId(sysResourceDO.getParentId());
        checkSysResourceDO.setResourceName(sysResourceDO.getResourceName());
        QueryWrapper<SysResourceDO> queryWrapper = new QueryWrapper<>(sysResourceDO);
        if (super.count(queryWrapper) > 0) {
            throw new BusinessException("同一目录下，菜单名称不能相同");
        }
        return super.save(sysResourceDO);
    }

    @Override
    @CacheEvict(value = AmCacheName.RESOURCE, allEntries = true)
    public boolean updateById(SysResourceDO sysResourceDO) {
        return super.updateById(sysResourceDO);
    }

    /**
     * 获取树模型结构数据
     *
     * @param SysResourceDOList
     * @param checkedMenuIds
     * @return List<SysTreeModel>
     * @author wanyong
     * @date 2017-12-19 10:55
     */
    private List<TreeDTO> convertTreeData(List<SysResourceDO> SysResourceDOList, Object[] checkedMenuIds) {
        Map<Long, List<TreeDTO>> map = new HashMap<>(3);
        for (SysResourceDO SysResourceDO : SysResourceDOList) {
            if (SysResourceDO != null && map.get(SysResourceDO.getParentId()) == null) {
                List<TreeDTO> children = new ArrayList<>();
                map.put(SysResourceDO.getParentId(), children);
            }
            map.get(SysResourceDO.getParentId()).add(convertTreeModel(SysResourceDO, checkedMenuIds));
        }
        List<TreeDTO> result = new ArrayList<>();
        for (SysResourceDO SysResourceDO : SysResourceDOList) {
            boolean flag = SysResourceDO != null && SysResourceDO.getParentId() == null || SysResourceDO.getParentId() == 0;
            if (flag) {
                TreeDTO treeDTO = convertTreeModel(SysResourceDO, checkedMenuIds);
                treeDTO.setChildren(getChild(map, SysResourceDO.getId()));
                result.add(treeDTO);
            }
        }
        return result;
    }

    /**
     * 递归获取子树模型结构数据
     *
     * @param map
     * @param id
     * @return List<SysTreeModel>
     * @author wanyong
     * @date 2017-12-19 10:56
     */
    private List<TreeDTO> getChild(Map<Long, List<TreeDTO>> map, Long id) {
        List<TreeDTO> treeModelList = map.get(id);
        if (treeModelList != null) {
            for (TreeDTO treeModel : treeModelList) {
                if (treeModel != null) {
                    treeModel.setChildren(getChild(map, treeModel.getId()));
                }
            }
        }
        return treeModelList;
    }

    /**
     * 把菜单模型对象转换成树模型对象
     *
     * @param sysResourceDO
     * @param checkedMenuIds
     * @return SysTreeModel
     * @author wanyong
     * @date 2017-12-19 14:22
     */
    private TreeDTO convertTreeModel(SysResourceDO sysResourceDO, Object[] checkedMenuIds) {
        TreeDTO treeDTO = new TreeDTO();
        treeDTO.setId(sysResourceDO.getId());
        treeDTO.setName(sysResourceDO.getResourceName());
        treeDTO.setIcon(sysResourceDO.getIcon());
        treeDTO.setSpread(true);
        treeDTO.setHref(sysResourceDO.getRequestUrl());
        treeDTO.setPermission(sysResourceDO.getPermission());
        treeDTO.setChecked(checkedMenuIds != null && ArrayUtil.contains(checkedMenuIds, sysResourceDO.getId()));
        treeDTO.setDisabled(false);
        return treeDTO;
    }
}
