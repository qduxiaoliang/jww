package com.jww.base.am.common;

/**
 * 通用常量类
 *
 * @author shadj
 * @date 2018/1/14 15:05
 */
public final class AmConstants {

    /**
     * 超级管理员权限标识
     */
    public static final String PERMISSION_ADMIN = "sys:admin";
    /**
     * 超级管理员用户编号
     */
    public static final Long USERID_ADMIN = 1L;

    /**
     * 服务模块使用的缓存名
     */
    public class AmCacheName {
        /**
         * 人才服务
         */
        public static final String USER = "sys_user";
        /**
         * 资源服务
         */
        public static final String RESOURCE = "sys_resource";
        /**
         * 部门服务
         */
        public static final String DEPT = "sys_dept";
        /**
         * 字典服务
         */
        public static final String DIC = "sys_dic";
        /**
         * 参数服务
         */
        public static final String PARAM = "sys_param";
        /**
         * 角色服务
         */
        public static final String ROLE = "sys_role";
        /**
         * 用户角色服务
         */
        public static final String USER_ROLE = "sys_user_role";
        /**
         * 角色资源服务
         */
        public static final String ROLE_RESOURCE = "sys_role_resource";
        /**
         * 权限服务
         */
        public static final String AUTHORIZATION = "sys_authorization";
    }
}
