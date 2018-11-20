package com.jww.common.web.util;

import cn.hutool.core.util.StrUtil;
import com.jww.common.core.constant.enums.ResultCodeEnum;
import com.jww.common.web.model.dto.Result;

/**
 * 返回结果工具类
 *
 * @author wanyong
 * @date 2017/11/11 20:28
 */
public class ResultUtil {

    public static Result ok() {
        return ok(null);
    }

    public static Result ok(Object object) {
        return new Result(ResultCodeEnum.SUCCESS.value(),
                ResultCodeEnum.SUCCESS.message(), object);
    }

    public static Result fail(ResultCodeEnum resultCodeEnum) {
        return new Result(resultCodeEnum.value(), resultCodeEnum.message(), null);
    }

    public static Result fail(int code, String message) {
        return new Result(code, message, null);
    }

    public static Result fail(ResultCodeEnum resultCodeEnum, String message) {
        return new Result(resultCodeEnum.value(), StrUtil.isBlank(message) ? resultCodeEnum.message() : message, null);
    }
}
