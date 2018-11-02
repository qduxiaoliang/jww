package com.jww.base.am.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.service.SysMenuService;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.common.AmConstants.AmCacheName;
import com.jww.base.am.dao.mapper.SysMenuMapper;
import com.jww.base.am.dao.mapper.SysRoleMenuMapper;
import com.jww.base.am.dao.mapper.SysTreeMapper;
import com.jww.base.am.model.entity.SysMenuEntity;
import com.jww.base.am.model.entity.SysTreeEntity;
import com.jww.common.core.annotation.DistributedLock;
import com.jww.common.core.base.BaseServiceImpl;
import com.jww.common.core.exception.BusinessException;
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
@Service("sysMenuService")
@CacheConfig(cacheNames = AmCacheName.MENU)
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenuEntity> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysTreeMapper sysTreeMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    @Cacheable
    public List<SysMenuEntity> queryList() {
        SysMenuEntity sysMenuEntity = new SysMenuEntity();
        // 状态为：启用
        sysMenuEntity.setEnable(1);
        // 是否删除：否
        sysMenuEntity.setIsDel(0);
        QueryWrapper<SysMenuEntity> entityWrapper = new QueryWrapper<>(sysMenuEntity);
        entityWrapper.orderByAsc("parent_id,sort_no");
        return super.list(entityWrapper);
    }

    @Override
    public IPage<SysMenuEntity> queryListPage(IPage<SysMenuEntity> page) {
        SysMenuEntity menu = new SysMenuEntity();
        menu.setEnable(1);
        menu.setIsDel(0);
        QueryWrapper<SysMenuEntity> wrapper = new QueryWrapper<>(menu);
        wrapper.eq("a.is_del", 0).eq("a.enable_", 1);
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
        return super.page(page, wrapper);
    }

    @Override
    @Cacheable
    public List<SysTreeEntity> queryMenuTreeByUserId(Long userId) {
        List<SysMenuEntity> sysMenuEntityList = null;
        // 如果是超级管理员，则查询所有目录菜单
        if (AmConstants.USERID_ADMIN.equals(userId)) {
            SysMenuEntity sysMenuEntity = new SysMenuEntity();
            sysMenuEntity.setEnable(1);
            sysMenuEntity.setIsShow(1);
            sysMenuEntity.setIsDel(0);
            QueryWrapper<SysMenuEntity> wrapper = new QueryWrapper<>(sysMenuEntity);
            wrapper.ne("menu_type", 2);
            wrapper.orderByAsc("parent_id,sort_no");
            sysMenuEntityList = sysMenuMapper.selectList(wrapper);
        } else {
            sysMenuEntityList = sysMenuMapper.selectMenuTreeByUserId(userId);
        }
        return convertTreeData(sysMenuEntityList, null);
    }

    @Override
    @Cacheable
    public List<SysTreeEntity> queryFuncMenuTree() {
        List<SysMenuEntity> sysMenuEntityList = queryList();
        return convertTreeData(sysMenuEntityList, null);
    }

    @Override
    @Cacheable(value = AmCacheName.ROLE)
    public List<SysTreeEntity> queryFuncMenuTree(Long roleId) {
        List<SysMenuEntity> sysMenuEntityList = queryList();
        List<Long> menuIdList = sysRoleMenuMapper.selectMenuIdListByRoleId(roleId);
        System.out.println("menuIdList:" + JSON.toJSONString(menuIdList));
        return convertTreeData(sysMenuEntityList, menuIdList.toArray());
    }

    @Override
    @Cacheable
    public List<SysTreeEntity> queryTree(Long id, Integer menuType) {
        List<SysTreeEntity> sysTreeEntityList = sysTreeMapper.selectMenuTree(id, menuType);
        List<SysTreeEntity> list = SysTreeEntity.getTree(sysTreeEntityList);
        return list;
    }

    @Override
    @DistributedLock
    @CacheEvict(value = AmCacheName.MENU, allEntries = true)
    public Boolean delete(Long id) {
        //查询是否有子菜单，如果有则返回false，否则允许删除
        QueryWrapper<SysMenuEntity> entityWrapper = new QueryWrapper<SysMenuEntity>();
        SysMenuEntity sysMenuEntity = new SysMenuEntity();
        sysMenuEntity.setParentId((Long) id);
        sysMenuEntity.setIsDel(0);
        entityWrapper.setEntity(sysMenuEntity);
        List<SysMenuEntity> childs = super.list(entityWrapper);
        if (CollUtil.isNotEmpty(childs)) {
            log.error("删除菜单[id:{}]失败，请先删除子菜单", id);
            throw new BusinessException("删除菜单失败，请先删除子菜单");
        }
        sysMenuEntity = new SysMenuEntity();
        sysMenuEntity.setId(id);
        sysMenuEntity.setIsDel(1);
        sysMenuEntity = super.modifyById(sysMenuEntity);
        if (sysMenuEntity != null) {
            return true;
        }
        return false;
    }

    @Override
    @CacheEvict(value = AmCacheName.MENU, allEntries = true)
    public Integer deleteBatch(Long[] ids) {
        int succ = 0;
        for (Long id : ids) {
            Boolean res = false;
            try {
                res = this.delete(id);
            } catch (Exception e) {
                log.error("删除菜单失败，id:{}", id, e);
            }
            if (res) {
                succ++;
            }
            log.debug("删除菜单完成，id:{}，执行结果：{}", id, res);
        }
        return succ;
    }

    /**
     * 查询所有父级菜单
     *
     * @return List<SysMenuModel>
     * @author shadj
     * @date 2018/1/25 22:37
     */
    @Override
    public List<SysMenuEntity> queryParentMenu() {
        return sysMenuMapper.selectParentMenu();
    }

    @Override
    @CacheEvict(value = AmCacheName.MENU, allEntries = true)
    @DistributedLock(value = "#sysMenuModel.getParentId()")
    public SysMenuEntity add(SysMenuEntity sysMenuEntity) {
        //名称重复验证，同一目录下，菜单名称不能相同
        SysMenuEntity menuModel = new SysMenuEntity();
        menuModel.setParentId(sysMenuEntity.getParentId());
        menuModel.setMenuName(sysMenuEntity.getMenuName());
        QueryWrapper<SysMenuEntity> entityWrapper = new QueryWrapper<>(menuModel);
        List<SysMenuEntity> sysMenuEntityList = super.list(entityWrapper);
        if (CollUtil.isNotEmpty(sysMenuEntityList)) {
            throw new BusinessException("同一目录下，菜单名称不能相同");
        }
        return super.add(sysMenuEntity);
    }

    @Override
    @CacheEvict(value = AmCacheName.MENU, allEntries = true)
    public SysMenuEntity modifyById(SysMenuEntity sysMenuEntity) {
        if (StrUtil.isNotBlank(sysMenuEntity.getMenuName())) {
            //名称重复验证，同一目录下，菜单名称不能相同（需要排除自己）
            SysMenuEntity menuModel = new SysMenuEntity();
            menuModel.setParentId(sysMenuEntity.getParentId());
            menuModel.setMenuName(sysMenuEntity.getMenuName());
            menuModel.setIsDel(0);
            QueryWrapper<SysMenuEntity> entityWrapper = new QueryWrapper<>(menuModel);
            entityWrapper.ne("id_", sysMenuEntity.getId());
            List<SysMenuEntity> sysMenuEntityList = super.list(entityWrapper);
            if (CollUtil.isNotEmpty(sysMenuEntityList)) {
                throw new BusinessException("同一目录下，菜单名称不能相同");
            }
        }
        return super.modifyById(sysMenuEntity);
    }

    /**
     * 获取树模型结构数据
     *
     * @param sysMenuEntityList
     * @param checkedMenuIds
     * @return List<SysTreeModel>
     * @author wanyong
     * @date 2017-12-19 10:55
     */
    private List<SysTreeEntity> convertTreeData(List<SysMenuEntity> sysMenuEntityList, Object[] checkedMenuIds) {
        Map<Long, List<SysTreeEntity>> map = new HashMap<>(3);
        for (SysMenuEntity sysMenuEntity : sysMenuEntityList) {
            if (sysMenuEntity != null && map.get(sysMenuEntity.getParentId()) == null) {
                List<SysTreeEntity> children = new ArrayList<>();
                map.put(sysMenuEntity.getParentId(), children);
            }
            map.get(sysMenuEntity.getParentId()).add(convertTreeModel(sysMenuEntity, checkedMenuIds));
        }
        List<SysTreeEntity> result = new ArrayList<>();
        for (SysMenuEntity sysMenuEntity : sysMenuEntityList) {
            boolean flag = sysMenuEntity != null && sysMenuEntity.getParentId() == null || sysMenuEntity.getParentId() == 0;
            if (flag) {
                SysTreeEntity sysTreeEntity = convertTreeModel(sysMenuEntity, checkedMenuIds);
                sysTreeEntity.setChildren(getChild(map, sysMenuEntity.getId()));
                result.add(sysTreeEntity);
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
    private List<SysTreeEntity> getChild(Map<Long, List<SysTreeEntity>> map, Long id) {
        List<SysTreeEntity> treeModelList = map.get(id);
        if (treeModelList != null) {
            for (SysTreeEntity treeModel : treeModelList) {
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
     * @param sysMenuEntity
     * @param checkedMenuIds
     * @return SysTreeModel
     * @author wanyong
     * @date 2017-12-19 14:22
     */
    private SysTreeEntity convertTreeModel(SysMenuEntity sysMenuEntity, Object[] checkedMenuIds) {
        SysTreeEntity sysTreeEntity = new SysTreeEntity();
        sysTreeEntity.setId(sysMenuEntity.getId());
        sysTreeEntity.setName(sysMenuEntity.getMenuName());
        sysTreeEntity.setIcon(sysMenuEntity.getIconcls());
        sysTreeEntity.setSpread(sysMenuEntity.getExpand() == 1);
        sysTreeEntity.setHref(sysMenuEntity.getRequest());
        sysTreeEntity.setPermission(sysMenuEntity.getPermission());
        sysTreeEntity.setChecked(checkedMenuIds != null && ArrayUtil.contains(checkedMenuIds, sysMenuEntity.getId()));
        sysTreeEntity.setDisabled(false);
        return sysTreeEntity;
    }
}
