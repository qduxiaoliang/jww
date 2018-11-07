package com.jww.base.am.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jww.common.core.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * 用户表实体
 *
 * @author wanyong
 * @date 2017-10-29
 */
@Data
@TableName("sys_user")
public class SysUserEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @TableField("username_")
    private String userName;

    /**
     * 姓名
     */
    @TableField("full_name")
    @Size(min = 1, max = 30, message = "用户姓名必须在1-30位之间")
    private String fullName;

    /**
     * 密码
     */
    @JsonIgnore
    @TableField("password_")
    private String password;

    /**
     * 随机盐
     */
    @JsonIgnore
    @TableField("salt_")
    private String salt;

    /**
     * 姓名
     */
    @TableField("user_type")
    private Integer userType;

    /**
     * 手机号
     */
    @TableField("phone_")
    private String phone;

    /**
     * 联系电话
     */
    @TableField("tel_")
    private String tel;

    /**
     * 头像
     */
    @TableField("avatar_")
    private String avatar;

    /**
     * 部门ID
     */
    @TableField("dept_id")
    private Long deptId;

    /**
     * 邮箱
     */
    @TableField("email_")
    private String email;

    /**
     * 启用标识（0-启用,1-禁用）
     */
    @TableField("is_enable")
    @ApiModelProperty(value = "启用标记（0-启用,1-禁用）", name = "isEnable")
    private Integer isEnable;

    /**
     * 删除标记
     */
    @TableLogic
    @TableField("is_del")
    @ApiModelProperty(value = "删除标记（0-正常,1-删除）", name = "isDel")
    private Integer isDel;

}
