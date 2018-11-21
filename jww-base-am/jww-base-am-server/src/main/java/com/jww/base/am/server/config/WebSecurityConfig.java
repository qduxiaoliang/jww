package com.jww.base.am.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全框架配置
 *
 * @author wanyong
 * @date 2018/11/6 20:47
 */
// @Configuration
// @EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/reg").permitAll()
                .antMatchers("/captcha/*").permitAll()
                .antMatchers("/logout").permitAll()
                // 静态资源不鉴权
                .antMatchers("/index.html").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/layui/**").permitAll()
                .antMatchers("/page/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                // swagger2不鉴权
                .antMatchers("/*/api-docs*").permitAll()
                .antMatchers("/callback*").permitAll()
                .antMatchers("/swagger*").permitAll()
                .antMatchers("/configuration/*").permitAll()
                .antMatchers("/*/configuration/*").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .anyRequest().authenticated();
    }
}
