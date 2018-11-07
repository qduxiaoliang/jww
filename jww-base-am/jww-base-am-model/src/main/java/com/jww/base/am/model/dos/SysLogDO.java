package com.jww.base.am.model.dos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jww.common.core.base.BaseDO;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 日志表Entity
 *
 * @author wanyong
 * @date 2018-01-22
 */
@Data
@TableName("sys_log")
public class SysLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 服务ID
     */
    @TableField("service_name")
    private String serviceName;

    /**
     * 用户名
     */
    @TableField("username_")
    private String username;

    /**
     * 姓名
     */
    @TableField("full_name")
    private String fullName;

    /**
     * 日志标题
     */
    @TableField("title_")
    private String title;

    /**
     * 操作类型(1:新增/2:查询/3:修改/4:删除/5:导入/6:导出)
     */
    @TableField("type_")
    private Integer type;

    /**
     * 用户代理
     */
    @TableField("user_agent")
    private String userAgent;

    /**
     * 请求URI
     */
    @TableField("request_uri")
    private String requestUri;

    /**
     * HTTP方式
     */
    @TableField("http_method")
    private String httpMethod;

    /**
     * 操作提交的数据
     */
    @TableField("params_")
    private String params;

    /**
     * 操作结果(0:失败/1:成功)
     */
    @TableField("result_")
    private Integer result;

    /**
     * 请求时间
     */
    @TableField("request_time")
    private Timestamp requestTime;

    /**
     * 执行时间
     */
    @TableField("time_")
    private Integer time;

    /**
     * 操作IP地址
     */
    @TableField("ip_")
    private String ip;

    /**
     * ip运营商
     */
    @TableField("ip_detail")
    private String ipDetail;

    /**
     * 异常信息
     */
    @TableField("exception_")
    private String exception;

    /**
     * 请求token
     */
    @TableField("request_token")
    private String requestToken;

    /**
     * 备注
     */
    @TableField("remark_")
    private String remark;
}
