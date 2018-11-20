package com.jww.base.am.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jww.base.am.dao.mapper.SysLogMapper;
import com.jww.base.am.model.dos.SysLogDO;
import com.jww.base.am.service.SysLogService;
import com.jww.common.core.base.BaseServiceImpl;
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
@Slf4j
@Service("sysLogService")
public class SysLogServiceImpl extends BaseServiceImpl<SysLogMapper, SysLogDO> implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public IPage<SysLogDO> listPage(IPage<SysLogDO> page) {
        QueryWrapper<SysLogDO> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(page.condition())) {
            StringBuilder conditionSql = new StringBuilder();
            Map<Object, Object> paramMap = page.condition();
            paramMap.forEach((k, v) -> {
                if (StrUtil.isNotBlank(v + "")) {
                    conditionSql.append(k + " like '%" + v + "%' OR ");
                }
            });
            if (StrUtil.isNotBlank(conditionSql)) {
                // entityWrapper.where(StrUtil.removeSuffix(conditionSql.toString(), "OR "));
            }
        }
        queryWrapper.orderByDesc("create_time");
        // page.setCondition(null);
        return super.page(page, queryWrapper);
    }

    @Override
    public boolean clearLog(Integer keepDays) {
        QueryWrapper<SysLogDO> queryWrapper = new QueryWrapper<>();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -keepDays);
        Date d = c.getTime();
        String day = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
        // entityWrapper.where("create_time < '" + day + "'");
        queryWrapper.le("create_time", day);
        return super.remove(queryWrapper);
    }
}
