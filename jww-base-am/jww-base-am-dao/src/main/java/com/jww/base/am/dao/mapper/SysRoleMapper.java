package com.jww.base.am.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.dos.SysRoleDO;
import com.jww.common.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色信息表 Mapper 接口
 *
 * @author wanyong
 * @date 2017-12-17
 */
public interface SysRoleMapper extends BaseMapper<SysRoleDO> {

    /**
     * 分页查询
     *
     * @param page    分页实体
     * @param wrapper wrapper条件
     * @return List<SysRoleModel>
     * @author wanyong
     * @date 2017-12-27 12:03
     */
    List<SysRoleDO> selectRoleList(IPage page, @Param("ew") Wrapper<SysRoleDO> wrapper);
}
