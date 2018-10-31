package com.jww.base.am.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jww.base.am.api.SysDicService;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.dao.mapper.SysDicMapper;
import com.jww.base.am.model.entity.SysDicEntity;
import com.jww.common.core.Constants;
import com.jww.common.core.base.BaseServiceImpl;
import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.StrUtil;
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
    public Page<SysDicEntity> queryListPage(Page<SysDicEntity> page) {
        SysDicEntity sysDicEntity = new SysDicEntity();
        sysDicEntity.setIsDel(0);
        EntityWrapper<SysDicEntity> entityWrapper = new EntityWrapper<>(sysDicEntity);
        if (ObjectUtil.isNotNull(page.getCondition())) {
            StringBuilder conditionSql = new StringBuilder();
            Map<String, Object> paramMap = page.getCondition();
            paramMap.forEach((k, v) -> {
                if (StrUtil.isNotBlank(v + "")) {
                    conditionSql.append(k + " like '%" + v + "%' AND ");
                }
            });
            entityWrapper.and(StrUtil.removeSuffix(conditionSql.toString(), "AND "));
        }
        entityWrapper.orderBy("typeText,sortNo");
        page.setCondition(null);
        return super.selectPage(page, entityWrapper);
    }

    @Override
    @Cacheable
    public List<SysDicEntity> queryTypeList() {
        EntityWrapper<SysDicEntity> entityWrapper = new EntityWrapper<>(new SysDicEntity());
        entityWrapper.setSqlSelect("DISTINCT type_text,type_");
        return super.selectList(entityWrapper);
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
        EntityWrapper<SysDicEntity> entityWrapper = new EntityWrapper<>(sysDicEntity);
        return super.selectList(entityWrapper);
    }

    @Override
    @CacheEvict(value = AmConstants.AmCacheName.DIC, allEntries = true)
    public boolean deleteBatchIds(List<? extends Serializable> idList) {
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
        EntityWrapper<SysDicEntity> entityWrapper = new EntityWrapper<>(sysDicEntity);
        return super.selectOne(entityWrapper);
    }
}
