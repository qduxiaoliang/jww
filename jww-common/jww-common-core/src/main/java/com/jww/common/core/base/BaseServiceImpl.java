package com.jww.common.core.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jww.common.core.constant.enums.ResultCodeEnum;
import com.jww.common.core.exception.BusinessException;

import java.util.Date;

/**
 * 业务处理基类实现
 *
 * @author wanyong
 * @date 2017/11/19 20:36
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity>
        extends ServiceImpl<BaseMapper<T>, T> implements BaseService<T> {

    public T add(T entity) {
        entity.setCreateTime(new Date());
        try {
            if (super.save(entity)) {
                return entity;
            }
        } catch (Exception exception) {
            String duplicateKey = "DuplicateKeyException";
            if (exception.toString().contains(duplicateKey)) {
                throw new BusinessException(ResultCodeEnum.DATA_DUPLICATE_KEY.message());
            }
            throw exception;
        }
        return null;
    }
}
