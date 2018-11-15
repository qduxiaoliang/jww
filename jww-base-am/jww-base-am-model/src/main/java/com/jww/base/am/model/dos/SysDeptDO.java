package com.jww.base.am.model.dos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jww.common.core.base.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 部门表Entity
 *
 * @author wanyong
 * @date 2018-01-22
 */
@Data
@TableName("sys_dept")
public class SysDeptDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 父ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 隶属单位ID
     */
    @TableField("unit_id")
    private Long unitId;

    /**
     * 部门名称
     */
    @TableField("dept_name")
    private String deptName;

    /**
     * 排序
     */
    @TableField("sort_no")
    private Integer sortNo;

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
