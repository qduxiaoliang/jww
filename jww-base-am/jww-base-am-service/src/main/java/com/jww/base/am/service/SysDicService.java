package com.jww.base.am.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.entity.SysDicEntity;
import com.jww.common.core.base.BaseService;

import java.util.List;

/**
 * <p>
 * 字典管理 服务类
 * </p>
 *
 * @author wanyong
 * @since 2017-12-17
 */
public interface SysDicService extends BaseService<SysDicEntity> {

    /**
     * 分页查找所有字典明细
     *
     * @param page
     * @return Page<SysDicModel>
     * @author wanyong
     * @date 2017/12/4 14:45
     */
    IPage<SysDicEntity> queryListPage(IPage<SysDicEntity> page);

    /**
     * 查询字典类型集合
     *
     * @return List<SysDicModel>
     * @author wanyong
     * @date 2018-01-10 13:01
     */
    List<SysDicEntity> queryTypeList();

    /**
     * 新增字典(新增前需要进行唯一主键检验)
     *
     * @param sysDicEntity
     * @return SysDicModel
     * @author wanyong
     * @date 2018-01-10 17:38
     */
    SysDicEntity add(SysDicEntity sysDicEntity);

    /**
     * 根据字典类型值查询字典集合
     *
     * @param type 字典类型值
     * @return List<SysDicModel>
     * @author wanyong
     * @date 2018-01-10 18:18
     */
    List<SysDicEntity> queryListByType(String type);

    /**
     * 根据字典类型和字典值查询字典
     *
     * @param type 字典类型
     * @param code 字典值
     * @return SysDicModel
     * @author wanyong
     * @date 2018-01-17 01:15
     */
    SysDicEntity queryByTypeAndCode(String type, String code);
}
