package com.jww.base.am.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.service.SysDicService;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.dao.mapper.SysDicMapper;
import com.jww.base.am.model.dos.SysDicDO;
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
public class SysDicServiceImpl extends BaseServiceImpl<SysDicMapper, SysDicDO> implements SysDicService {

    @Override
    public IPage<SysDicDO> queryListPage(IPage<SysDicDO> page) {
        SysDicDO sysDicDO = new SysDicDO();
        sysDicDO.setIsDel(0);
        QueryWrapper<SysDicDO> entityWrapper = new QueryWrapper<>(sysDicDO);
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
    public List<SysDicDO> queryTypeList() {
        QueryWrapper<SysDicDO> entityWrapper = new QueryWrapper<>(new SysDicDO());
        // entityWrapper.setSqlSelect("DISTINCT type_text,type_");
        return super.list(entityWrapper);
    }

    @Override
    @CacheEvict(value = AmConstants.AmCacheName.DIC, allEntries = true)
    public SysDicDO add(SysDicDO sysDicDO) {
        return super.add(sysDicDO);
    }

    @Override
    @Cacheable
    public List<SysDicDO> queryListByType(String type) {
        SysDicDO sysDicDO = new SysDicDO();
        sysDicDO.setType(type);
        sysDicDO.setIsDel(0);
        sysDicDO.setEnable(1);
        QueryWrapper<SysDicDO> entityWrapper = new QueryWrapper<>(sysDicDO);
        return super.list(entityWrapper);
    }

    @CacheEvict(value = AmConstants.AmCacheName.DIC, allEntries = true)
    public boolean removeByIds(List<? extends Serializable> idList) {
        List<SysDicDO> sysDicDOList = new ArrayList<SysDicDO>();
        idList.forEach(id -> {
            SysDicDO entity = new SysDicDO();
            entity.setId((Long) id);
            entity.setIsDel(1);
            entity.setUpdateTime(new Date());
            sysDicDOList.add(entity);
        });
        return super.updateBatchById(sysDicDOList);
    }

    @Override
    @Cacheable
    public SysDicDO queryByTypeAndCode(String type, String code) {
        SysDicDO sysDicDO = new SysDicDO();
        sysDicDO.setType(type);
        sysDicDO.setCode(code);
        sysDicDO.setIsDel(0);
        sysDicDO.setEnable(1);
        QueryWrapper<SysDicDO> entityWrapper = new QueryWrapper<>(sysDicDO);
        return super.getOne(entityWrapper);
    }
}
