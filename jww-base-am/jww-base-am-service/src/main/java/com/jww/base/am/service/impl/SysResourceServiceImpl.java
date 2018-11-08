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
import com.jww.base.am.dao.mapper.SysMenuMapper;
import com.jww.base.am.dao.mapper.SysRoleResourceMapper;
import com.jww.base.am.dao.mapper.SysTreeMapper;
import com.jww.base.am.model.dto.SysResourceDTO;
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
public class SysResourceServiceImpl extends BaseServiceImpl<SysMenuMapper, SysResourceDTO> implements SysResourceService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysTreeMapper sysTreeMapper;

    @Autowired
    private SysRoleResourceMapper sysRoleResourceMapper;

    @Override
    @Cacheable
    public List<SysResourceDTO> list() {
        SysResourceDTO sysResourceDTO = new SysResourceDTO();
        QueryWrapper<SysResourceDTO> queryWrapper = new QueryWrapper<>(sysResourceDTO);
        queryWrapper.orderByAsc("parent_id,sort_no");
        return super.list(queryWrapper);
    }

    @Override
    public IPage<SysResourceDTO> listPage(IPage<SysResourceDTO> page) {
        QueryWrapper<SysResourceDTO> queryWrapper = new QueryWrapper<>();
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
        List<SysResourceDTO> sysResourceDTOList;
        // 如果是超级管理员，则查询所有目录菜单
        if (AmConstants.USERID_ADMIN.equals(userId)) {
            SysResourceDTO sysResourceDTO = new SysResourceDTO();
            sysResourceDTO.setIsEnable(1);
            sysResourceDTO.setIsShow(1);
            sysResourceDTO.setResourceType(2);
            QueryWrapper<SysResourceDTO> queryWrapper = new QueryWrapper<>(sysResourceDTO);
            queryWrapper.orderByAsc("parent_id,sort_no");
            sysResourceDTOList = sysMenuMapper.selectList(queryWrapper);
        } else {
            sysResourceDTOList = sysMenuMapper.selectMenuTreeByUserId(userId);
        }
        return convertTreeData(sysResourceDTOList, null);
    }

    @Override
    @Cacheable
    public List<TreeDTO> listFuncMenuTree() {
        List<SysResourceDTO> sysResourceDTOList = list();
        return convertTreeData(sysResourceDTOList, null);
    }

    @Override
    @Cacheable(value = AmCacheName.ROLE)
    public List<TreeDTO> listFuncMenuTree(Long roleId) {
        List<SysResourceDTO> sysResourceDTOList = list();
        List<Long> menuIdList = sysRoleResourceMapper.selectMenuIdListByRoleId(roleId);
        return convertTreeData(sysResourceDTOList, menuIdList.toArray());
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
        SysResourceDTO sysResourceDTO = new SysResourceDTO();
        sysResourceDTO.setParentId(resourceId);
        QueryWrapper<SysResourceDTO> queryWrapper = new QueryWrapper<>(sysResourceDTO);
        List<SysResourceDTO> childList = super.list(queryWrapper);
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
    public List<SysResourceDTO> listParentMenu() {
        return sysMenuMapper.selectParentMenu();
    }

    @Override
    @CacheEvict(value = AmCacheName.RESOURCE, allEntries = true)
    @DistributedLock(value = "#sysMenuModel.getParentId()")
    public boolean save(SysResourceDTO sysResourceDTO) {
        // 名称重复验证，同一目录下，菜单名称不能相同
        SysResourceDTO checkSysResourceDTO = new SysResourceDTO();
        checkSysResourceDTO.setParentId(sysResourceDTO.getParentId());
        checkSysResourceDTO.setResourceName(sysResourceDTO.getResourceName());
        QueryWrapper<SysResourceDTO> queryWrapper = new QueryWrapper<>(sysResourceDTO);
        if (super.count(queryWrapper) > 0) {
            throw new BusinessException("同一目录下，菜单名称不能相同");
        }
        return super.save(sysResourceDTO);
    }

    @Override
    @CacheEvict(value = AmCacheName.RESOURCE, allEntries = true)
    public boolean updateById(SysResourceDTO sysResourceDTO) {
        return super.updateById(sysResourceDTO);
    }

    /**
     * 获取树模型结构数据
     *
     * @param SysResourceDTOList
     * @param checkedMenuIds
     * @return List<SysTreeModel>
     * @author wanyong
     * @date 2017-12-19 10:55
     */
    private List<TreeDTO> convertTreeData(List<SysResourceDTO> SysResourceDTOList, Object[] checkedMenuIds) {
        Map<Long, List<TreeDTO>> map = new HashMap<>(3);
        for (SysResourceDTO SysResourceDTO : SysResourceDTOList) {
            if (SysResourceDTO != null && map.get(SysResourceDTO.getParentId()) == null) {
                List<TreeDTO> children = new ArrayList<>();
                map.put(SysResourceDTO.getParentId(), children);
            }
            map.get(SysResourceDTO.getParentId()).add(convertTreeModel(SysResourceDTO, checkedMenuIds));
        }
        List<TreeDTO> result = new ArrayList<>();
        for (SysResourceDTO SysResourceDTO : SysResourceDTOList) {
            boolean flag = SysResourceDTO != null && SysResourceDTO.getParentId() == null || SysResourceDTO.getParentId() == 0;
            if (flag) {
                TreeDTO treeDTO = convertTreeModel(SysResourceDTO, checkedMenuIds);
                treeDTO.setChildren(getChild(map, SysResourceDTO.getId()));
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
     * @param sysResourceDTO
     * @param checkedMenuIds
     * @return SysTreeModel
     * @author wanyong
     * @date 2017-12-19 14:22
     */
    private TreeDTO convertTreeModel(SysResourceDTO sysResourceDTO, Object[] checkedMenuIds) {
        TreeDTO treeDTO = new TreeDTO();
        treeDTO.setId(sysResourceDTO.getId());
        treeDTO.setName(sysResourceDTO.getResourceName());
        treeDTO.setIcon(sysResourceDTO.getIcon());
        treeDTO.setSpread(true);
        treeDTO.setHref(sysResourceDTO.getRequestUrl());
        treeDTO.setPermission(sysResourceDTO.getPermission());
        treeDTO.setChecked(checkedMenuIds != null && ArrayUtil.contains(checkedMenuIds, sysResourceDTO.getId()));
        treeDTO.setDisabled(false);
        return treeDTO;
    }
}
