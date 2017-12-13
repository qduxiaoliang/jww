package com.jww.ump.rpc.api;

import com.jww.common.core.base.BaseService;
import com.jww.ump.model.UmpDeptModel;
import com.baomidou.mybatisplus.plugins.Page;
import com.jww.ump.model.UmpTreeModel;

import java.util.List;

/**
 * @Title:
 * @Description:
 * @Author: Ricky Wang
 * @Date: 17/12/1 11:26:33
 */
public interface UmpDeptService extends BaseService<UmpDeptModel> {
    Page<UmpDeptModel> queryListPage(Page<UmpDeptModel> page);

    boolean delBatchByIds(List<Long> ids);

    public UmpTreeModel queryTree();
}
