package com.jww.base.am.server.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.jww.base.am.common.util.IpUtil;
import com.jww.base.am.model.dos.SysLogDO;
import com.jww.base.am.model.dos.SysUserDO;
import com.jww.base.am.server.annotation.SysLogOpt;
import com.jww.base.am.server.async.AsyncTask;
import com.jww.common.core.constant.enums.LogOptEnum;
import com.jww.common.core.constant.enums.ResultCodeEnum;
import com.jww.common.core.util.RegexUtil;
import com.jww.common.web.model.dto.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * 日志入库切面
 *
 * @author RickyWang
 * @date 2017/12/27 13:56
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    /**
     * 开始时间
     */
    private long startTime = 0L;

    @Autowired
    private AsyncTask async;

    @Pointcut("execution(* *..controller..*.*(..)) && @annotation(com.jww.base.am.server.annotation.SysLogOpt)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        startTime = System.currentTimeMillis();
        Object result = null;
        SysLogDO sysLogDO = logPre(pjp);
        try {
            result = pjp.proceed();
        } finally {
            try {
                //查询类型不新增日志
                if (sysLogDO.getOperationType() != LogOptEnum.QUERY.value()) {
                    logAfter(result, sysLogDO);
                    if (!StrUtil.isBlank(sysLogDO.getUserName())) {
                        sysLogDO.setCreateTime(new Date());
                        sysLogDO.setUpdateTime(new Date());
                        async.logInsert(sysLogDO);
                    }
                }
            } catch (Exception e) {
                log.error("日志入库失败", e);
            }
        }
        return result;
    }


    private SysLogDO logPre(ProceedingJoinPoint pjp) throws Exception {
        SysLogDO sysLogDO = new SysLogDO();
        for (Method method : Class.forName(pjp.getTarget().getClass().getName()).getMethods()) {
            if (method.getName().equals(pjp.getSignature().getName())) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == pjp.getArgs().length) {
                    //方法名称
                    sysLogDO.setOperation(method.getAnnotation(SysLogOpt.class).value());
                    //操作类型
                    sysLogDO.setOperationType(method.getAnnotation(SysLogOpt.class).operationType().value());
                    break;
                }
            }
        }
        //获取请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        startTime = System.currentTimeMillis();
        String ip = HttpUtil.getClientIP(request);
        String ipDetail = IpUtil.getIpDetail(ip);
        //方法名含包名（com.jww.ump.SysLogController.queryListPage）
        String classMethod = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
        //请求参数
        String args = JSON.toJSONString(pjp.getArgs()).replaceAll(RegexUtil.getJSonValueRegex("password"), "****").replaceAll(RegexUtil.getJSonValueRegex("oldPassword"), "****");

        sysLogDO.setIp(ip);
        sysLogDO.setIpDetail(ipDetail);
        sysLogDO.setMethod(classMethod);
        sysLogDO.setParams(args);
        sysLogDO.setCreateTime(new Date());
        sysLogDO.setCreateBy(0L);
        sysLogDO.setUpdateBy(0L);
        // SysUserEntity currentUser = (SysUserEntity) WebUtil.getCurrentUser();
        SysUserDO currentUser = null;
        if (currentUser != null) {
            sysLogDO.setUserName(currentUser.getUserName());
            sysLogDO.setAccount(currentUser.getAccount());
        }
        return sysLogDO;
    }


    private boolean logAfter(Object result, SysLogDO sysLogDO) {
        if (sysLogDO.getUserName() == null) {
            // SysUserEntity currentUser = (SysUserEntity) WebUtil.getCurrentUser();
            SysUserDO currentUser = null;
            if (currentUser != null) {
                sysLogDO.setUserName(currentUser.getUserName());
                sysLogDO.setAccount(currentUser.getAccount());
            }
        }
        ResultDTO response = null;
        if (result != null) {
            response = (ResultDTO) result;
        }
        //返回结果
        if (response == null || response.code == ResultCodeEnum.SUCCESS.value()) {
            sysLogDO.setResult(1);
        } else {
            sysLogDO.setResult(0);
        }
        //执行时长(毫秒)
        Long spendTime = System.currentTimeMillis() - startTime;
        sysLogDO.setTime(spendTime);
        return true;
    }
}
