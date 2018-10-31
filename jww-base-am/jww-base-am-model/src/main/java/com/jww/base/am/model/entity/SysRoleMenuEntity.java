package com.jww.base.am.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jww.common.core.base.BaseEntity;
import lombok.Data;

/**
 * <p>
 * 角色授权表
 * </p>
 *
 * @author wanyong
 * @date 2017-11-29
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenuEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("role_id")
    private Long roleId;

    @TableField("menu_id")
    private Long menuId;

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
