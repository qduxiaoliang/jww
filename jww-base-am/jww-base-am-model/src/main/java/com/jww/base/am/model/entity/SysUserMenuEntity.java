package com.jww.base.am.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jww.common.core.base.BaseEntity;
import lombok.Data;

/**
 * <p>
 * 用户授权表
 * </p>
 *
 * @author waner
 * @date 2017-11-29
 */
@Data
@TableName("sys_user_menu")
public class SysUserMenuEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("user_id")
    private Long userId;
    @TableField("menu_id")
    private Long menuId;
    /**
     * 权限标识
     */
    @TableField("permission_")
    private String permission;
    /**
     * 是否启用
     */
    @TableField("enable_")
    private Integer enable;
    /**
     * 是否删除(0:未删除;1:已删除)
     */
    @TableField("is_del")
    private Integer isDel;
    /**
     * 备注
     */
    @TableField("remark_")
    private String remark;
}
