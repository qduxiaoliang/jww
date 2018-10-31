package com.jww.common.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统通用常量
 *
 * @author wanyong
 * @date 2017/11/10 11:20
 */
public final class CommonConstant {

    /**
     * 当前用户
     */
    public static final String CURRENT_USER = "CURRENT_USER";
    /**
     * 当前用户
     */
    public static final String CURRENT_USER_ID = "CURRENT_USER_ID";

    /**
     * 缓存键值
     */
    public static final Map<Class<?>, String> CACHE_KEY_MAP = new HashMap<>(5);

    /**
     * 缓存命名空间前缀
     */
    public static final String CACHE_NAMESPACE_PREFIX = "jww:";
}
