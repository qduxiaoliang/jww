package com.jww.base.am.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jww.base.am.api.SysLogService;
import com.jww.base.am.dao.mapper.SysLogMapper;
import com.jww.base.am.model.entity.SysLogEntity;
import com.jww.common.core.base.BaseServiceImpl;
import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author RickyWang
 * @since 2017-12-26
 */
@Service("sysLogService")
@Slf4j
public class SysLogServiceImpl extends BaseServiceImpl<SysLogMapper, SysLogEntity> implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public Page<SysLogEntity> queryListPage(Page<SysLogEntity> page) {
        EntityWrapper<SysLogEntity> entityWrapper = new EntityWrapper<>();
        if (ObjectUtil.isNotNull(page.getCondition())) {
            StringBuilder conditionSql = new StringBuilder();
            Map<String, Object> paramMap = page.getCondition();
            paramMap.forEach((k, v) -> {
                if (StrUtil.isNotBlank(v + "")) {
                    conditionSql.append(k + " like '%" + v + "%' OR ");
                }
            });
            if (StrUtil.isNotBlank(conditionSql)) {
                entityWrapper.where(StrUtil.removeSuffix(conditionSql.toString(), "OR "));
            }
        }
        entityWrapper.orderBy("create_time", false);
        page.setCondition(null);
        return super.selectPage(page, entityWrapper);
    }

    @Override
    public boolean clearLog(Integer keepDays) {
        EntityWrapper<SysLogEntity> entityWrapper = new EntityWrapper<>();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -keepDays);
        Date d = c.getTime();
        String day = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
        entityWrapper.where("create_time < '" + day + "'");
        return super.delete(entityWrapper);
    }
}
