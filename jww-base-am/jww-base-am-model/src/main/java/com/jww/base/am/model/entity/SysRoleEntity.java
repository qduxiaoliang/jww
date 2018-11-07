package com.jww.base.am.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jww.common.core.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 角色表实体
 *
 * @author wanyong
 * @date 2017-10-29
 */
@Data
@TableName("sys_role")
public class SysRoleEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 16, min = 1, message = "角色名称长度16位内")
    @Pattern(regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5]+$", message = "输入值限制：中文、字母、数字")
    @TableField("role_name")
    private String roleName;

    /**
     * 角色编码
     */
    @TableField("role_code")
    private String roleCode;

    /**
     * 角色描述
     */
    @TableField("role_desc")
    private String roleDesc;

    /**
     * 备注
     */
    @TableField("remark_")
    private String remark;

    /**
     * 启用标识（0-启用,1-禁用）
     */
    @ApiModelProperty(value = "启用标记（0-启用,1-禁用）", name = "isEnable")
    private Integer isEnable;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记（0-正常,1-删除）", name = "isDel")
    @TableField("is_del")
    @TableLogic
    private Integer isDel;

}
