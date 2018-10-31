package com.jww.common.core.constant.enums;

import com.jww.common.core.constant.CommonConstant;

/**
 * 缓存命名空间枚举
 *
 * @author wanyong
 * @date 2018/9/24 14:36
 */
public enum CacheNamespaceEnum {

    /**
     * 数据缓存
     */
    DATA(CommonConstant.CACHE_NAMESPACE_PREFIX + "data:", "数据缓存"),
    OAUTH(CommonConstant.CACHE_NAMESPACE_PREFIX + "oauth:", "授权认证缓存"),
    LOCK(CommonConstant.CACHE_NAMESPACE_PREFIX + "lock:", "分布式锁缓存"),
    CAPTCHA(CommonConstant.CACHE_NAMESPACE_PREFIX + "captcha:", "验证码缓存"),
    IP(CommonConstant.CACHE_NAMESPACE_PREFIX + "ip:", "ip归属地缓存");

    /**
     * 值
     */
    private String value;
    /**
     * 描述
     */
    private String message;

    CacheNamespaceEnum(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String value() {
        return this.value;
    }

    public String message() {
        return this.message;
    }
}
