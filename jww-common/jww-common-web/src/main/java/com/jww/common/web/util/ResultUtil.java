package com.jww.common.web.util;

import cn.hutool.core.util.StrUtil;
import com.jww.common.core.constant.enums.ResultCodeEnum;
import com.jww.common.web.model.ResultModel;

/**
 * 返回结果工具类
 *
 * @author wanyong
 * @date 2017/11/11 20:28
 */
public class ResultUtil {

    public static ResultModel ok() {
        return ok(null);
    }

    public static ResultModel ok(Object object) {
        return new ResultModel(ResultCodeEnum.SUCCESS.value(),
                ResultCodeEnum.SUCCESS.message(), object);
    }

    public static ResultModel fail(ResultCodeEnum resultCodeEnum) {
        return new ResultModel(resultCodeEnum.value(), resultCodeEnum.message(), null);
    }

    public static ResultModel fail(int code, String message) {
        return new ResultModel(code, message, null);
    }

    public static ResultModel fail(ResultCodeEnum resultCodeEnum, String message) {
        return new ResultModel(resultCodeEnum.value(), StrUtil.isBlank(message) ? resultCodeEnum.message() : message, null);
    }
}
