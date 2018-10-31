package com.jww.base.am.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.jww.base.am.model.SysDicModel;
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
public interface SysDicService extends BaseService<SysDicModel> {

    /**
     * 分页查找所有字典明细
     *
     * @param page
     * @return Page<SysDicModel>
     * @author wanyong
     * @date 2017/12/4 14:45
     */
    Page<SysDicModel> queryListPage(Page<SysDicModel> page);

    /**
     * 查询字典类型集合
     *
     * @return List<SysDicModel>
     * @author wanyong
     * @date 2018-01-10 13:01
     */
    List<SysDicModel> queryTypeList();

    /**
     * 新增字典(新增前需要进行唯一主键检验)
     *
     * @param sysDicModel
     * @return SysDicModel
     * @author wanyong
     * @date 2018-01-10 17:38
     */
    @Override
    SysDicModel add(SysDicModel sysDicModel);

    /**
     * 根据字典类型值查询字典集合
     *
     * @param type 字典类型值
     * @return List<SysDicModel>
     * @author wanyong
     * @date 2018-01-10 18:18
     */
    List<SysDicModel> queryListByType(String type);

    /**
     * 根据字典类型和字典值查询字典
     *
     * @param type 字典类型
     * @param code 字典值
     * @return SysDicModel
     * @author wanyong
     * @date 2018-01-17 01:15
     */
    SysDicModel queryByTypeAndCode(String type, String code);

}
