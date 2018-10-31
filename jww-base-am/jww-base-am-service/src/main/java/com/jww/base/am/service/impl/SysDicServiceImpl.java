package com.jww.base.am.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jww.base.am.api.SysDicService;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.dao.mapper.SysDicMapper;
import com.jww.base.am.model.SysDicModel;
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
public class SysDicServiceImpl extends BaseServiceImpl<SysDicMapper, SysDicModel> implements SysDicService {

    @Override
    public Page<SysDicModel> queryListPage(Page<SysDicModel> page) {
        SysDicModel sysDicModel = new SysDicModel();
        sysDicModel.setIsDel(0);
        EntityWrapper<SysDicModel> entityWrapper = new EntityWrapper<>(sysDicModel);
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
    public List<SysDicModel> queryTypeList() {
        EntityWrapper<SysDicModel> entityWrapper = new EntityWrapper<>(new SysDicModel());
        entityWrapper.setSqlSelect("DISTINCT type_text,type_");
        return super.selectList(entityWrapper);
    }

    @Override
    @CacheEvict(value = AmConstants.AmCacheName.DIC, allEntries = true)
    public SysDicModel add(SysDicModel sysDicModel) {
        return super.add(sysDicModel);
    }

    @Override
    @Cacheable
    public List<SysDicModel> queryListByType(String type) {
        SysDicModel sysDicModel = new SysDicModel();
        sysDicModel.setType(type);
        sysDicModel.setIsDel(0);
        sysDicModel.setEnable(1);
        EntityWrapper<SysDicModel> entityWrapper = new EntityWrapper<>(sysDicModel);
        return super.selectList(entityWrapper);
    }

    @Override
    @CacheEvict(value = AmConstants.AmCacheName.DIC, allEntries = true)
    public boolean deleteBatchIds(List<? extends Serializable> idList) {
        List<SysDicModel> sysDicModelList = new ArrayList<SysDicModel>();
        idList.forEach(id -> {
            SysDicModel entity = new SysDicModel();
            entity.setId((Long) id);
            entity.setIsDel(1);
            entity.setUpdateTime(new Date());
            sysDicModelList.add(entity);
        });
        return super.updateBatchById(sysDicModelList);
    }

    @Override
    @Cacheable
    public SysDicModel queryByTypeAndCode(String type, String code) {
        SysDicModel sysDicModel = new SysDicModel();
        sysDicModel.setType(type);
        sysDicModel.setCode(code);
        sysDicModel.setIsDel(0);
        sysDicModel.setEnable(1);
        EntityWrapper<SysDicModel> entityWrapper = new EntityWrapper<>(sysDicModel);
        return super.selectOne(entityWrapper);
    }
}
