package com.jww.base.am.server.async;

import com.jww.base.am.model.dos.SysLogDO;
import com.jww.base.am.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncTask {

    @Autowired
    private SysLogService sysLogService;

    @Async
    public void logInsert(SysLogDO t) {
        sysLogService.save(t);
    }
}
