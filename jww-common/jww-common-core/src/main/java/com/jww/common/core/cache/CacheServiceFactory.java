package com.jww.common.core.cache;

import com.jww.common.core.util.SpringContextUtil;

/**
 * 缓存服务工厂
 *
 * @author wanyong
 * @date 2018-11-21 16:10
 */
public class CacheServiceFactory {

    private CacheServiceFactory() {
    }

    public static CacheService getCacheService() {
        return SpringContextUtil.getBean(CacheService.class);
    }
}
