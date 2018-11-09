package com.jww.base.am.model.dto;

import com.jww.base.am.model.dos.SysRoleDO;
import lombok.Data;

/**
 * 角色传输实体
 *
 * @author wanyong
 * @date 2018-11-7 11:21
 */
@Data
public class SysRoleDTO extends SysRoleDO {

    /**
     * 部门名称
     */
    private String deptName;
}
