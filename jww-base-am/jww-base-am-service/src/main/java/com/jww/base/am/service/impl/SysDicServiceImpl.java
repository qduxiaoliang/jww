package com.jww.base.am.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.service.SysDicService;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.dao.mapper.SysDicMapper;
import com.jww.base.am.model.entity.SysDicEntity;
import com.jww.common.core.base.BaseServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典管理 服务实现类
 * </p>
 *
 * @author wanyong
 * @since 2017-12-17
 */
@Service("sysDicService")
@CacheConfig(cacheNames = AmConstants.AmCacheName.DIC)
public class SysDicServiceImpl extends BaseServiceImpl<SysDicMapper, SysDicEntity> implements SysDicService {

    @Override
    public IPage<SysDicEntity> queryListPage(IPage<SysDicEntity> page) {
        SysDicEntity sysDicEntity = new SysDicEntity();
        sysDicEntity.setIsDel(0);
        QueryWrapper<SysDicEntity> entityWrapper = new QueryWrapper<>(sysDicEntity);
        if (ObjectUtil.isNotNull(page.condition())) {
            StringBuilder conditionSql = new StringBuilder();
            Map<Object, Object> paramMap = page.condition();
            paramMap.forEach((k, v) -> {
                if (StrUtil.isNotBlank(v + "")) {
                    // conditionSql.append(k + " like '%" + v + "%' AND ");
                    entityWrapper.like(k + "", v);
                }
            });
            // entityWrapper.and(StrUtil.removeSuffix(conditionSql.toString(), "AND "));
        }
        // entityWrapper.orderBy("typeText,sortNo");
        entityWrapper.orderByAsc("typeText,sortNo");
        // page.setCondition(null);
        return super.page(page, entityWrapper);
    }

    @Override
    @Cacheable
    public List<SysDicEntity> queryTypeList() {
        QueryWrapper<SysDicEntity> entityWrapper = new QueryWrapper<>(new SysDicEntity());
        // entityWrapper.setSqlSelect("DISTINCT type_text,type_");
        return super.list(entityWrapper);
    }

    @Override
    @CacheEvict(value = AmConstants.AmCacheName.DIC, allEntries = true)
    public SysDicEntity add(SysDicEntity sysDicEntity) {
        return super.add(sysDicEntity);
    }

    @Override
    @Cacheable
    public List<SysDicEntity> queryListByType(String type) {
        SysDicEntity sysDicEntity = new SysDicEntity();
        sysDicEntity.setType(type);
        sysDicEntity.setIsDel(0);
        sysDicEntity.setEnable(1);
        QueryWrapper<SysDicEntity> entityWrapper = new QueryWrapper<>(sysDicEntity);
        return super.list(entityWrapper);
    }

    @CacheEvict(value = AmConstants.AmCacheName.DIC, allEntries = true)
    public boolean removeByIds(List<? extends Serializable> idList) {
        List<SysDicEntity> sysDicEntityList = new ArrayList<SysDicEntity>();
        idList.forEach(id -> {
            SysDicEntity entity = new SysDicEntity();
            entity.setId((Long) id);
            entity.setIsDel(1);
            entity.setUpdateTime(new Date());
            sysDicEntityList.add(entity);
        });
        return super.updateBatchById(sysDicEntityList);
    }

    @Override
    @Cacheable
    public SysDicEntity queryByTypeAndCode(String type, String code) {
        SysDicEntity sysDicEntity = new SysDicEntity();
        sysDicEntity.setType(type);
        sysDicEntity.setCode(code);
        sysDicEntity.setIsDel(0);
        sysDicEntity.setEnable(1);
        QueryWrapper<SysDicEntity> entityWrapper = new QueryWrapper<>(sysDicEntity);
        return super.getOne(entityWrapper);
    }
}
