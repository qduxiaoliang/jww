package com.jww.base.am.model.dto;

import com.jww.base.am.model.entity.SysUserEntity;
import lombok.Data;

/**
 * 用户传输实体
 *
 * @author wanyong
 * @date 2018-11-7 11:21
 */
@Data
public class SysUserDTO extends SysUserEntity {

    /**
     * 角色
     */
    private Long[] roleIds;
}
