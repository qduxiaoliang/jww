package com.jww.base.am.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.common.AmConstants;
import com.jww.base.am.dao.mapper.SysDicMapper;
import com.jww.base.am.model.dos.SysDicDO;
import com.jww.base.am.service.SysDicService;
import com.jww.common.core.base.BaseServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
    public IPage<SysDicDO> listPage(IPage<SysDicDO> page) {
        QueryWrapper<SysDicDO> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(page.condition())) {
            StringBuilder conditionSql = new StringBuilder();
            Map<Object, Object> paramMap = page.condition();
            paramMap.forEach((k, v) -> {
                if (StrUtil.isNotBlank(v + "")) {
                    // conditionSql.append(k + " like '%" + v + "%' AND ");
                    queryWrapper.like(k + "", v);
                }
            });
            // entityWrapper.and(StrUtil.removeSuffix(conditionSql.toString(), "AND "));
        }
        // entityWrapper.orderBy("typeText,sortNo");
        queryWrapper.orderByAsc("typeText,sortNo");
        // page.setCondition(null);
        return super.page(page, queryWrapper);
    }

    @Override
    @Cacheable
    public List<SysDicDO> listType() {
        QueryWrapper<SysDicDO> queryWrapper = new QueryWrapper<>(new SysDicDO());
        // entityWrapper.setSqlSelect("DISTINCT type_text,type_");
        return super.list(null);
    }

    @Override
    @CacheEvict(value = AmConstants.AmCacheName.DIC, allEntries = true)
    public boolean save(SysDicDO sysDicDO) {
        return super.save(sysDicDO);
    }

    @Override
    @Cacheable
    public List<SysDicDO> listByType(String type) {
        SysDicDO sysDicDO = new SysDicDO();
        sysDicDO.setType(type);
        QueryWrapper<SysDicDO> queryWrapper = new QueryWrapper<>(sysDicDO);
        return super.list(queryWrapper);
    }

    @Override
    @CacheEvict(value = AmConstants.AmCacheName.DIC, allEntries = true)
    public boolean removeByIds(List<Long> idList) {
        return super.removeByIds(idList);
    }

    @Override
    @Cacheable
    public SysDicDO getByTypeAndCode(String type, String code) {
        SysDicDO sysDicDO = new SysDicDO();
        sysDicDO.setType(type);
        sysDicDO.setCode(code);
        QueryWrapper<SysDicDO> queryWrapper = new QueryWrapper<>(sysDicDO);
        return super.getOne(queryWrapper);
    }
}
