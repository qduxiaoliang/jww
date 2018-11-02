package com.jww.base.am.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动类
 *
 * @author wanyong
 * @date 2018-11-1 15:06
 */
@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = {"com.jww.base.am.service.impl"})
@MapperScan(basePackages = {"com.jww.base.am.dao.mapper"})
public class JwwBaseAmServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JwwBaseAmServerApplication.class, args);
    }
}