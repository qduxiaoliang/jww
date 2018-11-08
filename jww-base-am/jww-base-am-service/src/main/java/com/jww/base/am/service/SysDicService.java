package com.jww.base.am.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.model.dto.SysDicDTO;
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
public interface SysDicService extends BaseService<SysDicDTO> {

    /**
     * 分页查找所有字典明细
     *
     * @param page 分页实体
     * @return IPage<SysDicDTO>
     * @author wanyong
     * @date 2017/12/4 14:45
     */
    IPage<SysDicDTO> listPage(IPage<SysDicDTO> page);

    /**
     * 查询字典类型集合
     *
     * @return List<SysDicDTO>
     * @author wanyong
     * @date 2018-01-10 13:01
     */
    List<SysDicDTO> listType();

    /**
     * 新增字典(新增前需要进行唯一主键检验)
     *
     * @param sysDicDTO
     * @return boolean
     * @author wanyong
     * @date 2018-01-10 17:38
     */
    @Override
    boolean save(SysDicDTO sysDicDTO);

    /**
     * 根据字典类型值查询字典集合
     *
     * @param type 字典类型值
     * @return List<SysDicDTO>
     * @author wanyong
     * @date 2018-01-10 18:18
     */
    List<SysDicDTO> listByType(String type);

    /**
     * 根据字典类型和字典值查询字典
     *
     * @param type 字典类型
     * @param code 字典值
     * @return SysDicDTO
     * @author wanyong
     * @date 2018-01-17 01:15
     */
    SysDicDTO getByTypeAndCode(String type, String code);

    /**
     * 根据字典ID集合批量删除
     *
     * @param idList 字典ID集合
     * @return boolean
     * @author wanyong
     * @date 2018-11-8 15:38
     */
    boolean removeByIds(List<Long> idList);
}