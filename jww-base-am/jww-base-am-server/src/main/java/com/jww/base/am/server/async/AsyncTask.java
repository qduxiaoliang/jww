package com.jww.base.am.server.async;

import com.jww.base.am.api.SysLogService;
import com.jww.base.am.model.entity.SysLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncTask {

    @Autowired
    private SysLogService sysLogService;

    @Async
    public void logInsert(SysLogEntity t){
        sysLogService.insert(t);
    }
}