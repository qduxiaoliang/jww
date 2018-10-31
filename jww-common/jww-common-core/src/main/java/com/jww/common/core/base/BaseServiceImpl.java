package com.jww.common.core.base;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 业务处理基类实现
 *
 * @author wanyong
 * @date 2017/11/19 20:36
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity>
        extends ServiceImpl<BaseMapper<T>, T> implements BaseService<T> {
}
