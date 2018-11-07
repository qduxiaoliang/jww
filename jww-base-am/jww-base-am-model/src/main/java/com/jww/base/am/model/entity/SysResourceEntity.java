package com.jww.base.am.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jww.common.core.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资源权限表实体
 *
 * @author wanyong
 * @date 2017-11-08
 */
@Data
@TableName("sys_resource")
public class SysResourceEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单名称
     */
    @TableField("resource_name")
    private String resourceName;

    /**
     * 菜单权限标识
     */
    @TableField("permission_")
    private String permission;

    /**
     * 请求链接
     */
    @TableField("request_url")
    private String requestUrl;

    /**
     * 请求方法
     */
    @TableField("method_")
    private String method;

    /**
     * 父菜单ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 图标
     */
    @TableField("icon_")
    private String icon;

    /**
     * VUE页面
     */
    @TableField("component_")
    private String component;

    /**
     * 系统类型
     */
    @TableField("sys_type")
    private String sysType;

    /**
     * 排序值
     */
    @TableField("sort_no")
    private Integer sortNo;

    /**
     * 资源等级
     */
    @TableField("resource_level")
    private Integer resourceLevel;

    /**
     * 资源类型 （0菜单 1按钮）
     */
    @TableField("resource_type")
    private String resourceType;

    /**
     * 前端URL
     */
    @TableField("path_")
    private String path;

    /**
     * 备注
     */
    @TableField("remark_")
    private String remark;

    /**
     * 删除标记
     */
    @TableLogic
    @ApiModelProperty(value = "删除标记（0-正常,1-删除）", name = "isDel")
    @TableField("is_del")
    private Integer isDel;

    /**
     * 启用标记
     */
    @ApiModelProperty(value = "启用标记（0-启用,1-禁用）", name = "isEnable")
    @TableField("is_enable")
    private Integer isEnable;

    /**
     * 是否显示
     */
    @ApiModelProperty(value = "是否显示(0:不显示/1:显示)", name = "isShow")
    @TableField("is_show")
    private Integer isShow;

}
