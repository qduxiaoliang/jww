package com.jww.base.am.dao.mapper;

import com.jww.base.am.model.dos.SysResourceDO;
import com.jww.common.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单 Mapper 接口
 * </p>
 *
 * @author wanyong
 * @since 2017-11-29
 */
public interface SysResourceMapper extends BaseMapper<SysResourceDO> {

    /**
     * 根据用户ID查询菜单树
     *
     * @param userId
     * @return List<SysMenuModel>
     * @author wanyong
     * @date 2017-12-03 00:51
     */
    List<SysResourceDO> selectMenuTreeByUserId(@Param("userId") Long userId);

    /**
     * 查询所有父级菜单
     *
     * @return List<SysMenuModel>
     * @author shadj
     * @date 2018/1/25 22:37
     */
    List<SysResourceDO> selectParentMenu();
}