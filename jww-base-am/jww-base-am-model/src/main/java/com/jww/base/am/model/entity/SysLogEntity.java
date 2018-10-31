package com.jww.base.am.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jww.common.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 日志表
 *
 * @author RickyWang
 * @date 17/12/26 12:41:27
 */
@ToString
@TableName("sys_log")
public class SysLogEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 登陆帐户
     */
    @TableField("account_")
    @Getter
    @Setter
    private String account;
    /**
     * 用户名
     */
    @TableField("user_name")
    @Getter
    @Setter
    private String userName;
    /**
     * 用户操作
     */
    @TableField("operation_")
    @Getter
    @Setter
    private String operation;
    /**
     * 操作类型
     */
    @TableField("operation_type")
    @Getter
    @Setter
    private Integer operationType;
    /**
     * 请求方法
     */
    @TableField("method_")
    @Getter
    @Setter
    private String method;
    /**
     * 请求参数
     */
    @TableField("params_")
    @Getter
    @Setter
    private String params;
    /**
     * 操作结果
     */
    @TableField("result_")
    @Getter
    @Setter
    private Integer result;
    /**
     * 执行时长(毫秒)
     */
    @TableField("time_")
    @Getter
    @Setter
    private Long time;
    /**
     * IP地址
     */
    @TableField("ip_")
    @Getter
    @Setter
    private String ip;
    /**
     * IP地址详情
     */
    @TableField("ip_detail")
    @Getter
    @Setter
    private String ipDetail;
}
