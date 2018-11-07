package com.jww.base.am.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jww.common.core.base.BaseEntity;
import lombok.Data;

/**
 * 角色部门关系表实体
 *
 * @author wanyong
 * @date 2017-10-29
 */
@Data
@TableName("sys_role_dept")
public class SysRoleDeptEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 部门ID
     */
    @TableField("dept_id")
    private Long deptId;
}
