package com.jww.base.am.dao.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.dos.SysDeptDO;
import com.jww.common.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门 Mapper 接口
 *
 * @author wanyong
 * @date 2017-11-25
 */
public interface SysDeptMapper extends BaseMapper<SysDeptDO> {

    /**
     * 分页查询
     *
     * @param page     分页参数
     * @param deptName 部门名称
     * @return List<SysDeptDO>
     * @author wanyong
     * @date 2017-12-27 11:57
     */
    List<SysDeptDO> selectPage(IPage<SysDeptDO> page, @Param("deptName") String deptName);
}
