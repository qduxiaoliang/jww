package com.jww.common.redis.config;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.jww.common.core.base.BaseDO;
import com.jww.common.core.constant.enums.CacheNamespaceEnum;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.*;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Redis核心配置
 *
 * @author wanyong
 * @date 2018-11-14 16:13
 */
@EnableCaching
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig extends CachingConfigurerSupport {

    @Resource
    private LettuceConnectionFactory lettuceConnectionFactory;

    /**
     * <p>
     * 自定义KEY生成器，格式： jww:data:[包名 + 类名 + 方法名+ 参数]
     * 如：jww:data:com.jww.common.redis.RedisConfig:queryList_param1_param2
     * </p>
     *
     * @return KeyGenerator
     * @author wanyong
     * @date 2017/11/21 15:38
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (Object target, Method method, Object... params) -> {
            CacheConfig cacheConfig = target.getClass().getAnnotation(CacheConfig.class);
            Cacheable cacheable = method.getAnnotation(Cacheable.class);
            CachePut cachePut = method.getAnnotation(CachePut.class);
            CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);
            String cacheName = "";
            if (cacheable != null) {
                String[] cacheNames = cacheable.value();
                if (ArrayUtil.isNotEmpty(cacheNames)) {
                    cacheName = cacheNames[0];
                }
            } else if (cachePut != null) {
                String[] cacheNames = cachePut.value();
                if (ArrayUtil.isNotEmpty(cacheNames)) {
                    cacheName = cacheNames[0];
                }
            } else if (cacheEvict != null) {
                String[] cacheNames = cacheEvict.value();
                if (ArrayUtil.isNotEmpty(cacheNames)) {
                    cacheName = cacheNames[0];
                }
            }
            if (cacheConfig != null && StrUtil.isBlank(cacheName)) {
                String[] cacheNames = cacheConfig.cacheNames();
                if (ArrayUtil.isNotEmpty(cacheNames)) {
                    cacheName = cacheNames[0];
                }
            }
            if (StrUtil.isBlank(cacheName)) {
                cacheName = "default";
            }
            String paramStr = getParamStr(params);
            return CacheNamespaceEnum.DATA.value() + cacheName + ":" + target.getClass().getName() + ":"
                    + method.getName() + paramStr;
        };
    }

    /**
     * 缓存管理器配置：主要作用是为了关闭缓存key默认前缀规则
     *
     * @param lettuceConnectionFactory 连接工厂
     * @return RedisCacheManager
     * @author wanyong
     * @date 2018-11-15 15:24
     */
    @Bean
    public RedisCacheManager cacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .disableKeyPrefix()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        return RedisCacheManager.builder(lettuceConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .transactionAware()
                .build();
    }

    /**
     * 获取参数串（BaseDO取ID），以下划线连线
     *
     * @param params 参数
     * @return String
     * @author wanyong
     * @date 2018-11-15 14:46
     */
    private String getParamStr(Object[] params) {
        if (ArrayUtil.isEmpty(params)) {
            return "";
        }
        String paramStr = "";
        for (Object param : params) {
            // BaseDO类型，取ID
            if (param instanceof BaseDO) {
                BaseDO baseDO = (BaseDO) param;
                paramStr = String.join("_", paramStr, String.valueOf(baseDO.getId()));
            } else if (ArrayUtil.isArray(param)) {
                // 数组类型，将各元素序列化为字符串
                Object[] arrs = (Object[]) param;
                paramStr = Arrays.stream(arrs).map(JSON::toJSONString).collect(Collectors.joining("_"));
            } else {
                // 其它类型，直接序列化为字符串
                paramStr = String.join("_", paramStr, JSON.toJSONString(param));
            }
        }
        return paramStr;
    }
}
