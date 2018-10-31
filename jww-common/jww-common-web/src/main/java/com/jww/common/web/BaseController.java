package com.jww.common.web;

/**
 * 控制器基类
 *
 * @author wanyong
 * @date 2017/11/9 23:45
 */
public abstract class BaseController {

    /**
     * 获取当前用户Id
     *
     * @return Long
     * @author wanyong
     * @date 2017-11-30 17:45
     */
    protected Long getCurrentUserId() {
        return null;
    }

    /**
     * 获取当前用户
     *
     * @return Object
     * @author wanyong
     * @date 2017-11-30 17:45
     */
    protected Object getCurrentUser() {
        return null;
    }
}
