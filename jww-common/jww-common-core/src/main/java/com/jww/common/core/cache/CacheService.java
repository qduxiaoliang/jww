package com.jww.common.core.cache;

/**
 * 缓存服务接口
 *
 * @author wanyong
 * @date 2018-11-21 16:10
 */
public interface CacheService {

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return boolean
     * @author wanyong
     * @date 2018-11-21 16:12
     */
    boolean hasKey(String key);

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return boolean
     * @author wanyong
     * @date 2018-11-21 16:13
     */
    boolean set(String key, Object value);

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return boolean true成功 false 失败
     * @author wanyong
     * @date 2018-11-21 16:13
     */
    boolean set(String key, Object value, long time);

    /**
     * 删除缓存
     *
     * @param key 键
     * @author wanyong
     * @date 2018-11-21 16:14
     */
    void del(String... key);

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return Object 值
     * @author wanyong
     * @date 2018-11-21 16:15
     */
    Object get(String key);
}
