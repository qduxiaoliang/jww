package com.jww.base.am.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jww.common.core.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 参数表Entity
 *
 * @author wanyong
 * @date 2018-01-22
 */
@Data
@TableName("sys_param")
public class SysParamEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 参数键名
     */
    @TableField("param_key")
    private String paramKey;

    /**
     * 参数键值
     */
    @TableField("param_value")
    private String paramValue;

    /**
     * 参数分类ID
     */
    @TableField("catalog_id")
    private Long catalogId;

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
