package com.jww.common.redis.configuration;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jww.common.core.Constants;
import com.jww.common.core.base.BaseModel;
import com.xiaoleilu.hutool.util.ArrayUtil;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author shadj
 * @description: Redis缓存配置类
 * @date 2017/11/24 10:19
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * <p>
     * 自定义KEY生成器，格式： jww:data:[包名 + 类名 + 方法名+ 参数]
     * 如：jww:data:com.jww.common.redis.RedisConfig:queryList_param1_param2
     * </p>
     *
     * @return KeyGenerator
     * @author shadj
     * @date 2017/11/21 15:38
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                String paramStr = getParamStr(params);
                StringBuilder sb = new StringBuilder();
                sb.append(Constants.DATA_CACHE_NAMESPACE)
                        .append(target.getClass().getName())
                        .append(":").append(method.getName())
                        .append("_").append(paramStr);
                return sb.toString();
            }

            /** 获取参数串（BaseModel取ID），以下划线连线 */
            private String getParamStr(Object[] params) {
                if (ArrayUtil.isEmpty(params)) {
                    return "";
                }
                String paramStr = "";
                for (Object param : params) {
                    //BaseModel类型，取ID
                    if (param instanceof BaseModel) {
                        BaseModel model = (BaseModel) param;
                        paramStr = String.join("_", paramStr, String.valueOf(model.getId()));
                    } else if (ArrayUtil.isArray(param)) {
                        //数组类型，将各元素序列化为字符串
                        Object[] arrs = (Object[]) param;
                        paramStr = Arrays.stream(arrs).map(JSON::toJSONString).collect(Collectors.joining("_"));
                    } else {
                        //其它类型，直接序列化为字符串
                        paramStr = String.join("_", paramStr, JSON.toJSONString(param));
                    }
                }
                return paramStr;
            }
        };
    }


    /**
     * @param redisTemplate
     * @return org.springframework.cache.CacheManager
     * @description: 初始化缓存管理类
     * @author shadj
     * @date 2017/11/24 14:47
     */
    @SuppressWarnings("rawtypes")
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        return rcm;
    }

    /**
     * @return RedisTemplate
     * @description: RedisTemplate序列化方式设置，并初始化
     * @param: factory
     * @author shadj
     * @date 2017/11/21 15:23
     */
    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<String, Serializable>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //KEY 为String方式
        template.setKeySerializer(template.getStringSerializer());
        //VALUE 使用 jackson进行序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

}
