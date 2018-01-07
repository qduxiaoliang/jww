package com.jww.ump.server.aspect;

import com.alibaba.fastjson.JSON;
import com.jww.common.core.Constants;
import com.jww.common.core.exception.BusinessException;
import com.jww.common.core.exception.LoginException;
import com.jww.common.core.util.RegexUtil;
import com.jww.common.web.model.ResultModel;
import com.jww.ump.model.SysLogModel;
import com.jww.ump.model.SysUserModel;
import com.jww.ump.rpc.api.SysLogService;
import com.jww.ump.server.annotation.SysLogOpt;
import com.xiaoleilu.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
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
    private SysLogService logService;

    @Pointcut("execution(* *..controller..*.*(..)) && @annotation(com.jww.base.am.server.annotation.SysLogOpt)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        startTime = System.currentTimeMillis();
        Object result = null;
        boolean isQueryType = false;
        SysUserModel crrentUser = null;
        SysLogModel sysLogModel = logPre(pjp);
        try {
            result = pjp.proceed();
        } finally {
            //查询类型不添加日志
            if(!(sysLogModel.getOperationType()==Constants.LogOptEnum.QUERY.value() || sysLogModel.getOperationType() ==Constants.LogOptEnum.UNKNOW.value())
                    && logAfter(result,sysLogModel).getUserName()!=null){
                logService.add(sysLogModel);
            }
        }
        return result;
    }


    private SysLogModel logPre(ProceedingJoinPoint pjp) throws Exception{
        //操作类型
        Integer operationType = null;
        //方法名称
        String operation = null;
        for (Method method : Class.forName(pjp.getTarget().getClass().getName()).getMethods()) {
            if (method.getName().equals(pjp.getSignature().getName())) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == pjp.getArgs().length) {
                    //方法名称
                    operation = method.getAnnotation(SysLogOpt.class).value();
                    //操作类型
                    operationType = method.getAnnotation(SysLogOpt.class).operationType().value();
                    break;
                }
            }
        }
        //获取请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        startTime = System.currentTimeMillis();
        String ip = HttpUtil.getClientIP(request);
        //方法名含包名（com.jww.ump.SysLogController.queryListPage）
        String classMethod = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
        //请求参数
        String args = JSON.toJSONString(pjp.getArgs()).replaceAll(RegexUtil.getJSonValueRegex("password"),"****").replaceAll(RegexUtil.getJSonValueRegex("oldPassword"),"****");
        SysLogModel sysLogModel = new SysLogModel();
        sysLogModel.setIp(ip);
        sysLogModel.setMethod(classMethod);
        sysLogModel.setParams(args);
        sysLogModel.setCreateTime(new Date());
        sysLogModel.setCreateBy(0L);
        sysLogModel.setUpdateBy(0L);
        //方法名称
        sysLogModel.setOperation(operation);
        //操作类型
        sysLogModel.setOperationType(operationType);
        SysUserModel crrentUser = (SysUserModel) SecurityUtils.getSubject().getPrincipal();
        if(crrentUser!=null){
            sysLogModel.setUserName(crrentUser.getUserName());
        }
        return sysLogModel;
    }


    private SysLogModel logAfter(Object result, SysLogModel sysLogModel) {
        boolean hasLogin = false;
        ResultModel response = null;
        if(result!=null){
            response = (ResultModel)result;
        }
        if(sysLogModel.getUserName()==null){
            SysUserModel crrentUser = (SysUserModel) SecurityUtils.getSubject().getPrincipal();
            if(crrentUser!=null){
                sysLogModel.setUserName(crrentUser.getUserName());
                hasLogin = true;
            }
        }
        //返回结果
        if(response!=null && response.code == Constants.ResultCodeEnum.SUCCESS.value()){
            sysLogModel.setResult(1);
        }else{
            sysLogModel.setResult(0);
        }
        //执行时长(毫秒)
        Long spendTime = System.currentTimeMillis() - startTime;
        sysLogModel.setTime(spendTime);
        return sysLogModel;
    }
}
