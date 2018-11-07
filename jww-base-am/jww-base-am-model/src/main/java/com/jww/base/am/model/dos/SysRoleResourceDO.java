package com.jww.base.am.model.dos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jww.common.core.base.BaseDO;
import lombok.Data;

/**
 * 角色资源关系表实体
 *
 * @author wanyong
 * @date 2017-10-29
 */
@Data
@TableName("sys_role_resource")
public class SysRoleResourceDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 菜单ID
     */
    @TableField("resource_id")
    private Long resourceId;
}
