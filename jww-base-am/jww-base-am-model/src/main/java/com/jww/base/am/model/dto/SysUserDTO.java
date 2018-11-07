package com.jww.base.am.model.dto;

import com.jww.base.am.model.dos.SysUserDO;
import lombok.Data;

/**
 * 用户传输实体
 *
 * @author wanyong
 * @date 2018-11-7 11:21
 */
@Data
public class SysUserDTO extends SysUserDO {

    /**
     * 角色
     */
    private Long[] roleIds;
}
