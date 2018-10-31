package com.jww.common.db.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * MybatisPlus配置
 *
 * @author wanyong
 * @date 2018/6/6 10:32
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * SQL执行效率插件【生产环境可以关闭】
     *
     * @return PerformanceInterceptor
     * @author wanyong
     * @date 2017/11/19 11:59
     */
    @Profile("dev")
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * 分页插件
     *
     * @return PaginationInterceptor
     * @author wanyong
     * @date 2018/9/9 22:17
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}