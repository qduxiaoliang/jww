package com.jww.base.am.model.dos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jww.common.core.base.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户角色表实体
 *
 * @author wanyong
 * @date 2017-10-29
 */
@Data
@TableName("sys_user_role")
public class SysUserRoleDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记（0-正常,1-删除）", name = "isDel")
    @TableField("is_del")
    @TableLogic
    private Integer isDel;
}
