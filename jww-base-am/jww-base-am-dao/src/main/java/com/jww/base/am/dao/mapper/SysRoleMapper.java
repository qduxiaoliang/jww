package com.jww.base.am.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.jww.base.am.model.entity.SysRoleEntity;
import com.jww.common.core.base.BaseMapper;
import javafx.scene.control.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色信息表 Mapper 接口
 *
 * @author wanyong
 * @date 2017-12-17
 */
public interface SysRoleMapper extends BaseMapper<SysRoleEntity> {

    /**
     * 分页查询
     *
     * @param page    分页实体
     * @param wrapper wrapper条件
     * @return List<SysRoleModel>
     * @author wanyong
     * @date 2017-12-27 12:03
     */
    List<SysRoleEntity> selectRoleList(Pagination page, @Param("ew") Wrapper<SysRoleEntity> wrapper);
}
