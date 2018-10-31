package com.jww.common.web.handler;

import cn.hutool.core.util.StrUtil;
import com.jww.common.core.base.BaseException;
import com.jww.common.core.constant.enums.ResultCodeEnum;
import com.jww.common.core.exception.BusinessException;
import com.jww.common.web.model.ResultModel;
import com.jww.common.web.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局的的异常拦截器
 *
 * @author wanyong
 * @date 2018/7/18 22:13
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常
     *
     * @param e 异常
     * @return Result
     * @author wanyong
     * @since 2018/7/18 22:12
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResultModel exception(Exception e) {
        log.info("保存全局异常信息，message={}", e.getMessage(), e);
        // 媒体类型
        if (e instanceof HttpMediaTypeNotSupportedException) {
            return ResultUtil.fail(ResultCodeEnum.NO_SUPPORTED_MEDIATYPE);
        }
        // SpringBoot参数验证框架如果验证失败则抛出MethodArgumentNotValidException异常
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            FieldError fieldError = methodArgumentNotValidException.getBindingResult().getFieldError();
            return ResultUtil.fail(ResultCodeEnum.BAD_REQUEST, fieldError.getDefaultMessage());
        }
        // Assert参数校验会抛出IllegalArgumentException异常
        if (e instanceof IllegalArgumentException) {
            String jwtNotFoundMessage = "JWT String argument cannot be null or empty";
            if (StrUtil.isNotBlank(e.getMessage()) && e.getMessage().contains(jwtNotFoundMessage)) {
                return ResultUtil.fail(ResultCodeEnum.BAD_REQUEST, "非法请求，拒绝访问");
            }
            return ResultUtil.fail(ResultCodeEnum.BAD_REQUEST, e.getMessage());
        }
        if (e instanceof BaseException) {
            BaseException baseException = (BaseException) e;
            return ResultUtil.fail(baseException.getCode(), baseException.getMessage());
        }
        String duplicateKeyException = "MySQLIntegrityConstraintViolationException";
        if (StrUtil.isNotBlank(e.getMessage()) && e.getMessage().contains(duplicateKeyException)) {
            return ResultUtil.fail(ResultCodeEnum.DATA_DUPLICATE_KEY);
        }
        if (e instanceof RuntimeException && StrUtil.isNotBlank(e.getMessage()) && e.getMessage().contains(BusinessException.class.getName())) {
            String message = e.getMessage().substring(BusinessException.class.getName().length() + 1, e.getMessage().indexOf("\r\n")).trim();
            return ResultUtil.fail(ResultCodeEnum.INTERNAL_SERVER_ERROR, message);
        }
        return ResultUtil.fail(ResultCodeEnum.INTERNAL_SERVER_ERROR);
    }
}
