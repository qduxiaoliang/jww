package com.jww.base.am.server.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.jww.base.am.common.util.IpUtil;
import com.jww.base.am.model.dos.SysUserDO;
import com.jww.base.am.model.dto.SysLogDTO;
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
        SysLogDTO sysLogDTO = logPre(pjp);
        try {
            result = pjp.proceed();
        } finally {
            try {
                //查询类型不新增日志
                if (sysLogDTO.getType() != LogOptEnum.QUERY.value()) {
                    logAfter(result, sysLogDTO);
                    if (!StrUtil.isBlank(sysLogDTO.getUsername())) {
                        sysLogDTO.setCreateTime(new Date());
                        sysLogDTO.setUpdateTime(new Date());
                        async.logInsert(sysLogDTO);
                    }
                }
            } catch (Exception e) {
                log.error("日志入库失败", e);
            }
        }
        return result;
    }


    private SysLogDTO logPre(ProceedingJoinPoint pjp) throws Exception {
        SysLogDTO sysLogDTO = new SysLogDTO();
        for (Method method : Class.forName(pjp.getTarget().getClass().getName()).getMethods()) {
            if (method.getName().equals(pjp.getSignature().getName())) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == pjp.getArgs().length) {
                    // 方法名称
                    sysLogDTO.setRequestUri(method.getAnnotation(SysLogOpt.class).value());
                    // 操作类型
                    sysLogDTO.setType(method.getAnnotation(SysLogOpt.class).operationType().value());
                    break;
                }
            }
        }
        // 获取请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        startTime = System.currentTimeMillis();
        String ip = HttpUtil.getClientIP(request);
        String ipDetail = IpUtil.getIpDetail(ip);
        //方法名含包名（com.jww.ump.SysLogController.queryListPage）
        String classMethod = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
        //请求参数
        String args = JSON.toJSONString(pjp.getArgs()).replaceAll(RegexUtil.getJSonValueRegex("password"), "****").replaceAll(RegexUtil.getJSonValueRegex("oldPassword"), "****");

        sysLogDTO.setIp(ip);
        sysLogDTO.setIpDetail(ipDetail);
        sysLogDTO.setRequestUri(classMethod);
        sysLogDTO.setParams(args);
        sysLogDTO.setCreateTime(new Date());
        sysLogDTO.setCreateBy(0L);
        sysLogDTO.setUpdateBy(0L);
        // SysUserEntity currentUser = (SysUserEntity) WebUtil.getCurrentUser();
        SysUserDO currentUser = null;
        if (currentUser != null) {
            sysLogDTO.setUsername(currentUser.getUsername());
            sysLogDTO.setFullName(currentUser.getFullName());
        }
        return sysLogDTO;
    }


    private boolean logAfter(Object result, SysLogDTO sysLogDTO) {
        if (sysLogDTO.getFullName() == null) {
            // SysUserEntity currentUser = (SysUserEntity) WebUtil.getCurrentUser();
            SysUserDO currentUser = null;
            if (currentUser != null) {
                sysLogDTO.setUsername(currentUser.getUsername());
                sysLogDTO.setFullName(currentUser.getFullName());
            }
        }
        ResultDTO response = null;
        if (result != null) {
            response = (ResultDTO) result;
        }
        //返回结果
        if (response == null || response.code == ResultCodeEnum.SUCCESS.value()) {
            sysLogDTO.setResult(1);
        } else {
            sysLogDTO.setResult(0);
        }
        //执行时长(毫秒)
        Long spendTime = System.currentTimeMillis() - startTime;
        sysLogDTO.setTime(spendTime);
        return true;
    }
}
