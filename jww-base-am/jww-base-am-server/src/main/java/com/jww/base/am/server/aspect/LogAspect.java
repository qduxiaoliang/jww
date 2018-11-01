package com.jww.base.am.server.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.jww.base.am.common.util.IpUtil;
import com.jww.base.am.model.entity.SysLogEntity;
import com.jww.base.am.model.entity.SysUserEntity;
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
        SysLogEntity sysLogEntity = logPre(pjp);
        try {
            result = pjp.proceed();
        } finally {
            try {
                //查询类型不新增日志
                if (sysLogEntity.getOperationType() != LogOptEnum.QUERY.value()) {
                    logAfter(result, sysLogEntity);
                    if (!StrUtil.isBlank(sysLogEntity.getUserName())) {
                        sysLogEntity.setCreateTime(new Date());
                        sysLogEntity.setUpdateTime(new Date());
                        async.logInsert(sysLogEntity);
                    }
                }
            } catch (Exception e) {
                log.error("日志入库失败", e);
            }
        }
        return result;
    }


    private SysLogEntity logPre(ProceedingJoinPoint pjp) throws Exception {
        SysLogEntity sysLogEntity = new SysLogEntity();
        for (Method method : Class.forName(pjp.getTarget().getClass().getName()).getMethods()) {
            if (method.getName().equals(pjp.getSignature().getName())) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == pjp.getArgs().length) {
                    //方法名称
                    sysLogEntity.setOperation(method.getAnnotation(SysLogOpt.class).value());
                    //操作类型
                    sysLogEntity.setOperationType(method.getAnnotation(SysLogOpt.class).operationType().value());
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

        sysLogEntity.setIp(ip);
        sysLogEntity.setIpDetail(ipDetail);
        sysLogEntity.setMethod(classMethod);
        sysLogEntity.setParams(args);
        sysLogEntity.setCreateTime(new Date());
        sysLogEntity.setCreateBy(0L);
        sysLogEntity.setUpdateBy(0L);
        // SysUserEntity currentUser = (SysUserEntity) WebUtil.getCurrentUser();
        SysUserEntity currentUser = null;
        if (currentUser != null) {
            sysLogEntity.setUserName(currentUser.getUserName());
            sysLogEntity.setAccount(currentUser.getAccount());
        }
        return sysLogEntity;
    }


    private boolean logAfter(Object result, SysLogEntity sysLogEntity) {
        if (sysLogEntity.getUserName() == null) {
            // SysUserEntity currentUser = (SysUserEntity) WebUtil.getCurrentUser();
            SysUserEntity currentUser = null;
            if (currentUser != null) {
                sysLogEntity.setUserName(currentUser.getUserName());
                sysLogEntity.setAccount(currentUser.getAccount());
            }
        }
        ResultDTO response = null;
        if (result != null) {
            response = (ResultDTO) result;
        }
        //返回结果
        if (response == null || response.code == ResultCodeEnum.SUCCESS.value()) {
            sysLogEntity.setResult(1);
        } else {
            sysLogEntity.setResult(0);
        }
        //执行时长(毫秒)
        Long spendTime = System.currentTimeMillis() - startTime;
        sysLogEntity.setTime(spendTime);
        return true;
    }
}
