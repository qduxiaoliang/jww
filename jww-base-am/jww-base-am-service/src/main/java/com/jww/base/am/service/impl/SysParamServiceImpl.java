package com.jww.base.am.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.service.SysParamService;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.dao.mapper.SysParamMapper;
import com.jww.base.am.model.entity.SysParamEntity;
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
 * 全局参数表 服务实现类
 * </p>
 *
 * @author shadj
 * @since 2017-12-24
 */
@Service("sysParamService")
@CacheConfig(cacheNames = AmConstants.AmCacheName.PARAM)
public class SysParamServiceImpl extends BaseServiceImpl<SysParamMapper, SysParamEntity> implements SysParamService {

    @Override
    public IPage<SysParamEntity> queryListPage(IPage<SysParamEntity> page) {
        SysParamEntity sysParamEntity = new SysParamEntity();
        sysParamEntity.setIsDel(0);
        QueryWrapper<SysParamEntity> queryWrapper = new QueryWrapper<>(sysParamEntity);
        if (ObjectUtil.isNotNull(page.condition())) {
            StringBuilder conditionSql = new StringBuilder();
            Map<Object, Object> paramMap = page.condition();
            paramMap.forEach((k, v) -> {
                if (StrUtil.isNotBlank(v + "")) {
                    conditionSql.append(k + " like '%" + v + "%' AND ");
                }
            });
            // queryWrapper.and(StrUtil.removeSuffix(conditionSql.toString(), "AND "));
        }
        // page.setCondition(null);
        return super.page(page, queryWrapper);
    }

    @CacheEvict(value = AmConstants.AmCacheName.PARAM, allEntries = true)
    public SysParamEntity add(SysParamEntity paramModel) {
        super.save(paramModel);
        return paramModel;
    }

    @CacheEvict(value = AmConstants.AmCacheName.PARAM, allEntries = true)
    public boolean deleteBatchIds(List<? extends Serializable> idList) {
        List<SysParamEntity> sysParamEntityList = new ArrayList<SysParamEntity>();
        idList.forEach(id -> {
            SysParamEntity entity = new SysParamEntity();
            entity.setId((Long) id);
            entity.setIsDel(1);
            entity.setUpdateTime(new Date());
            sysParamEntityList.add(entity);
        });
        return super.updateBatchById(sysParamEntityList);
    }

    @Override
    @Cacheable
    public SysParamEntity queryByParamKey(String paramKey) {
        SysParamEntity sysParamEntity = new SysParamEntity();
        sysParamEntity.setEnable(1);
        sysParamEntity.setIsDel(0);
        sysParamEntity.setParamKey(paramKey);
        QueryWrapper<SysParamEntity> entityWrapper = new QueryWrapper<>(sysParamEntity);
        return super.getOne(entityWrapper);
    }
}
