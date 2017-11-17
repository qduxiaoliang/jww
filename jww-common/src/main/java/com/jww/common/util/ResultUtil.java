package com.jww.common.util;

import com.jww.common.Constants;
import com.jww.common.model.ResultModel;

/**
 * @author wanyong
 * @description: 返回结果工具类
 * @date 2017/11/11 20:28
 */
public class ResultUtil {

    public static ResultModel ok(Object object) {
        return new ResultModel(Constants.ResultCodeEnum.SUCCESS.value(),
                Constants.ResultCodeEnum.SUCCESS.getMessage(), object);
    }

    public static ResultModel fail(Constants.ResultCodeEnum resultCodeEnum) {
        return new ResultModel(resultCodeEnum.value(), resultCodeEnum.getMessage(), null);
    }
}