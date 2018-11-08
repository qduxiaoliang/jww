package com.jww.base.am.model.dos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jww.common.core.base.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 字典表Entity
 *
 * @author wanyong
 * @date 2018-01-22
 */
@Data
@TableName("sys_dic")
public class SysDicDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 类型
     */
    @TableField("type_")
    private String type;

    /**
     * 字典类型名称
     */
    @TableField("type_name")
    private String typeName;

    /**
     * 数据值
     */
    @TableField("code_")
    private String code;

    /**
     * 数据值名称
     */
    @TableField("code_name")
    private String codeName;

    /**
     * 排序（升序）
     */
    @TableField("sort_no")
    private Integer sortNo;

    /**
     * 父id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 是否可编辑（0:不可编辑/1:可编辑）
     */
    @TableField("is_editable")
    private Integer isEditable;

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
