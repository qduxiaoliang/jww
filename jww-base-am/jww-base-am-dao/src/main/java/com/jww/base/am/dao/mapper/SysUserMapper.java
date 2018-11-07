package com.jww.base.am.dao.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.entity.SysUserEntity;
import com.jww.common.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表 Mapper 接口
 *
 * @author wanyong
 * @date 2017/11/17 15:51
 */
public interface SysUserMapper extends BaseMapper<SysUserEntity> {

    /**
     * 根据关键字分页查询
     *
     * @param page      分页对象
     * @param searchKey 关键字
     * @return List<SysUserModel>
     * @author wanyong
     * @date 2017-12-27 12:06
     */
    List<SysUserEntity> selectPage(IPage<SysUserEntity> page, @Param("searchKey") String searchKey);
}
