package com.jww.base.am.server.annotation;

import com.jww.common.core.constant.enums.LogOptEnum;

import java.lang.annotation.*;

/**
 * 日志注解
 *
 * @author RickyWang
 * @date 2017/12/27 13:56
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLogOpt {
    String value();

    String module() default "";  //模块名称 系统管理-用户管理－列表页面

    String description() default "";  //描述

    LogOptEnum operationType() default LogOptEnum.UNKNOW;//操作类型
}
