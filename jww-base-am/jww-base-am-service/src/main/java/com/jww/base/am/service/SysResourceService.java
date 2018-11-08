package com.jww.base.am.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.dto.SysResourceDTO;
import com.jww.common.core.base.BaseService;
import com.jww.common.core.model.dto.TreeDTO;

import java.util.List;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author wanyong
 * @since 2017-11-29
 */
public interface SysResourceService extends BaseService<SysResourceDTO> {

    /**
     * 查找所有菜单
     *
     * @return List<SysResourceDTO>
     * @author wanyong
     * @date 2017-12-02 13:59
     */
    List<SysResourceDTO> list();

    /**
     * 分页查询所有菜单
     *
     * @param page 分页对象
     * @return IPage<SysResourceDTO>
     * @author shadj
     * @date 2017/12/18 13:52
     */
    IPage<SysResourceDTO> listPage(IPage<SysResourceDTO> page);

    /**
     * 根据用户ID查找菜单树（包含目录和菜单，不包含按钮）
     *
     * @param userId
     * @return List<TreeDTO>
     * @author wanyong
     * @date 2017-12-03 00:56
     */
    List<TreeDTO> listMenuTreeByUserId(Long userId);

    /**
     * 查找功能菜单树（包含目录、菜单和按钮）
     *
     * @return List<TreeModel>
     * @author wanyong
     * @date 2017-12-19 11:14
     */
    List<TreeDTO> listFuncMenuTree();

    /**
     * 根据角色ID查找功能菜单树（包含目录、菜单和按钮）
     *
     * @param roleId
     * @return List<TreeDTO>
     * @author wanyong
     * @date 2017-12-19 11:14
     */
    List<TreeDTO> listFuncMenuTree(Long roleId);

    /**
     * 查询菜单树，供页面选择父菜单使用，过滤自己及子菜单
     *
     * @param id
     * @param menuType
     * @return List<TreeDTO>
     * @author shadj
     * @date 2017/12/22 22:59
     */
    List<TreeDTO> listTree(Long id, Integer menuType);

    /**
     * 删除单个菜单
     *
     * @param resourceId 菜单编号
     * @return boolean 删除成功返回true,否则返回false
     * @author shadj
     * @date 2017/12/23 23:20
     */
    boolean removeById(Long resourceId);

    /**
     * 批量删除菜单
     *
     * @param resourceIds 要删除的菜单编号数组
     * @return boolean
     * @author shadj
     * @date 2017/12/24 14:06
     */
    boolean removeByIds(Long[] resourceIds);

    /**
     * 查询所有父级菜单
     *
     * @return List<SysResourceDTO>
     * @author shadj
     * @date 2018/1/25 22:37
     */
    List<SysResourceDTO> listParentMenu();
}
