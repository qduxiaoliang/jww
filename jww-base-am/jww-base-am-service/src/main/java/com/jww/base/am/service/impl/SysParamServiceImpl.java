package com.jww.base.am.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.dao.mapper.SysParamMapper;
import com.jww.base.am.model.dos.SysParamDO;
import com.jww.base.am.service.SysParamService;
import com.jww.common.core.base.BaseServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
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
    public IPage<SysParamDO> listPage(IPage<SysParamDO> page) {
        QueryWrapper<SysParamDO> queryWrapper = new QueryWrapper<>();
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

    @Override
    @CacheEvict(value = AmConstants.AmCacheName.PARAM, allEntries = true)
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return super.removeByIds(idList);
    }

    @Override
    @CacheEvict(value = AmConstants.AmCacheName.PARAM, allEntries = true)
    public boolean save(SysParamDO sysParamDO) {
        return super.save(sysParamDO);
    }

    @Override
    @Cacheable
    public SysParamDO getByParamKey(String paramKey) {
        SysParamDO sysParamDO = new SysParamDO();
        sysParamDO.setParamKey(paramKey);
        QueryWrapper<SysParamDO> queryWrapper = new QueryWrapper<>(sysParamDO);
        return super.getOne(queryWrapper);
    }
}
