package com.jww.base.am.server.async;

import com.jww.base.am.model.entity.SysLogEntity;
import com.jww.base.am.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncTask {

    @Autowired
    private SysLogService sysLogService;

    @Async
    public void logInsert(SysLogEntity t) {
        sysLogService.save(t);
    }
}
