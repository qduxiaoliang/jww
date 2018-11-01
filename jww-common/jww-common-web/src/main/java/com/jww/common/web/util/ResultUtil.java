package com.jww.common.web.util;

import cn.hutool.core.util.StrUtil;
import com.jww.common.core.constant.enums.ResultCodeEnum;
import com.jww.common.web.model.dto.ResultDTO;

/**
 * 返回结果工具类
 *
 * @author wanyong
 * @date 2017/11/11 20:28
 */
public class ResultUtil {

    public static ResultDTO ok() {
        return ok(null);
    }

    public static ResultDTO ok(Object object) {
        return new ResultDTO(ResultCodeEnum.SUCCESS.value(),
                ResultCodeEnum.SUCCESS.message(), object);
    }

    public static ResultDTO fail(ResultCodeEnum resultCodeEnum) {
        return new ResultDTO(resultCodeEnum.value(), resultCodeEnum.message(), null);
    }

    public static ResultDTO fail(int code, String message) {
        return new ResultDTO(code, message, null);
    }

    public static ResultDTO fail(ResultCodeEnum resultCodeEnum, String message) {
        return new ResultDTO(resultCodeEnum.value(), StrUtil.isBlank(message) ? resultCodeEnum.message() : message, null);
    }
}
