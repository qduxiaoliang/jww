package com.jww.common.ship.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 快递服务配置
 *
 * @author shadj
 * @date 2018/5/2 14:58
 */
@Component
@ConfigurationProperties(prefix = "ship")
public class ShipProperties {

    /**
     * 快递100免费版分配的身份授权key
     */
    @Setter@Getter
    private String id;
    /**
     * 快递100企业版分配的customer ID
     */
    @Setter@Getter
    private String customer;
    /**
     * 快递100企业版的授权key
     */
    @Setter@Getter
    private String key;
    /**
     * 是否是测试环境，企业版的测试环境URL不一样
     */
    @Setter@Getter
    private boolean isTest;

}
