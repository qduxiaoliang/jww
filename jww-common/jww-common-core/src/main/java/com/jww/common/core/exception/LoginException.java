package com.jww.common.core.exception;

import com.jww.common.core.base.BaseException;
import com.jww.common.core.constant.enums.ResultCodeEnum;

/**
 * 登录异常类
 *
 * @author wanyong
 * @date 2017/11/10 11:24
 */
public class LoginException extends BaseException {

    public LoginException() {
        super();
    }

    public LoginException(Throwable ex) {
        super(ex);
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable ex) {
        super(message, ex);
    }

    @Override
    public ResultCodeEnum getCode() {
        return ResultCodeEnum.LOGIN_FAIL;
    }
}
