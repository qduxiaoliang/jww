package com.jww.common.log.aspect;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.jww.common.web.ResultModel;
import com.xiaoleilu.hutool.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * @Title: 控制层日志切面
 * @Description:
 * @Author: Ricky Wang
 * @Date: 17/11/26 20:47:21
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {
    // 开始时间
    private long startTime = 0L;

    @Pointcut("execution(* *..controller..*.*(..))")
    public void webLogPointCut() {

    }

    @Around("webLogPointCut()")
    public Object doAround(ProceedingJoinPoint pjp){

        startTime = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        // 记录下请求内容
        HttpServletRequest request = attributes.getRequest();
        StringBuffer logbf = new StringBuffer();
        logbf.append("request:{url:").append(request.getRequestURL());
        logbf.append(",httpMethod:").append(request.getMethod());
        logbf.append(",ip:").append(request.getRemoteAddr());
        logbf.append(",classMethod:").append(pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
        logbf.append(",args:").append(Arrays.toString(pjp.getArgs()));
        logbf.append(",startTime:").append(DateUtil.date(startTime));
        Object result = null;
        try{
            result = pjp.proceed();
        }catch (Throwable throwable){
            log.error(throwable.getMessage());
            log.error(Arrays.toString(throwable.getStackTrace()));
        }
        // 处理完请求，返回内容
        logbf.append("},response:").append(JSON.toJSONString(result));
        logbf.append(",spendTime:").append(System.currentTimeMillis() - startTime + "ms}");
        String logStr = logbf.toString();
        if(result==null){
            log.error(logStr);
        }
        log.info(logStr);
        return result;
    }
}
