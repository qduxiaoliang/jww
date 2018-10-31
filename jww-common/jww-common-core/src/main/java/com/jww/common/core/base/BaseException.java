package com.jww.common.core.base;

import com.jww.common.core.constant.enums.ResultCodeEnum;

/**
 * 统一异常基类
 *
 * @author wanyong
 * @date 2018/7/18 22:08
 */
public abstract class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(Throwable ex) {
        super(ex);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable ex) {
        super(message, ex);
    }

    /**
     * 获取错误码
     *
     * @return ResultCodeEnum 错误码
     * @author wanyong
     * @date 2018/7/18 22:08
     */
    public abstract ResultCodeEnum getCode();

}
