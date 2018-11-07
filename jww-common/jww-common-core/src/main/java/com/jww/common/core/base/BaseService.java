package com.jww.common.core.base;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 业务处理基类接口
 *
 * @author wanyong
 * @date 2017/11/12 11:56
 */
public interface BaseService<N extends BaseDO> extends IService<N> {

    /**
     * 新增
     *
     * @param entity 实体
     * @return T
     * @author wanyong
     * @date 2017/12/6 13:28
     */
    T add(T entity);
}
