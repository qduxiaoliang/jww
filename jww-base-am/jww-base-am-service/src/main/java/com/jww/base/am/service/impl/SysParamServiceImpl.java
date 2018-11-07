package com.jww.base.am.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.service.SysParamService;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.dao.mapper.SysParamMapper;
import com.jww.base.am.model.dos.SysParamDO;
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
public class SysParamServiceImpl extends BaseServiceImpl<SysParamMapper, SysParamDO> implements SysParamService {

    @Override
    public IPage<SysParamDO> queryListPage(IPage<SysParamDO> page) {
        SysParamDO sysParamDO = new SysParamDO();
        sysParamDO.setIsDel(0);
        QueryWrapper<SysParamDO> queryWrapper = new QueryWrapper<>(sysParamDO);
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
    public SysParamDO add(SysParamDO paramModel) {
        super.save(paramModel);
        return paramModel;
    }

    @CacheEvict(value = AmConstants.AmCacheName.PARAM, allEntries = true)
    public boolean deleteBatchIds(List<? extends Serializable> idList) {
        List<SysParamDO> sysParamDOList = new ArrayList<SysParamDO>();
        idList.forEach(id -> {
            SysParamDO entity = new SysParamDO();
            entity.setId((Long) id);
            entity.setIsDel(1);
            entity.setUpdateTime(new Date());
            sysParamDOList.add(entity);
        });
        return super.updateBatchById(sysParamDOList);
    }

    @Override
    @Cacheable
    public SysParamDO queryByParamKey(String paramKey) {
        SysParamDO sysParamDO = new SysParamDO();
        sysParamDO.setEnable(1);
        sysParamDO.setIsDel(0);
        sysParamDO.setParamKey(paramKey);
        QueryWrapper<SysParamDO> entityWrapper = new QueryWrapper<>(sysParamDO);
        return super.getOne(entityWrapper);
    }
}
