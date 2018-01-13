package com.jww.common.core.base;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jww.common.core.Constants;
import com.jww.common.core.exception.BusinessException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.Date;
import java.util.List;

/**
 * 业务处理基类实现
 *
 * @author wanyong
 * @date 2017/11/19 20:36
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseModel>
        extends ServiceImpl<BaseMapper<T>, T> implements BaseService<T> {

    @Override
    @CacheEvict(value = Constants.CACHE_VALUE, allEntries = true)
    public T modifyById(T entity) {
        T resultEntity = null;
        entity.setUpdateTime(new Date());
        if (super.updateById(entity)) {
            resultEntity = entity;
        }
        return resultEntity;
    }

    @Override
    @Cacheable(value = Constants.CACHE_VALUE)
    public T queryById(Long id) {
        return super.selectById(id);
    }

    @Override
    @CachePut(Constants.CACHE_VALUE)
    public T add(T entity) {
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        try {
            if (super.insert(entity)) {
                return entity;
            }
        } catch (Exception exception) {
            String duplicateKey = "DuplicateKeyException";
            if (exception.toString().contains(duplicateKey)) {
                throw new BusinessException(Constants.ResultCodeEnum.DATA_DUPLICATE_KEY.getMessage());
            }
            throw exception;
        }
        return null;
    }

    @Override
    public boolean delBatchByIds(T entity, List<Long> ids) {
        ids.forEach(id -> {
            entity.setId(id);
            entity.setIsDel(1);
            this.modifyById(entity);
        });
        return true;
    }
}
