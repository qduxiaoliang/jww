package com.jww.common.core.constant.enums;

/**
 * 返回码枚举
 *
 * @author wanyong
 * @date 2018/7/18 21:52
 */
public enum ResultCodeEnum {
    /**
     * 成功
     */
    SUCCESS(200, "成功"),
    REGISTER_FAIL_ACCOUNT_EXIST(302, "账号已存在"),
    REGISTER_FAIL_ACCOUNT_PHONE_EXIST(302002, "手机号已被占用"),
    LOGIN_FAIL(303, "登录失败"),
    LOGIN_FAIL_ACCOUNT_LOCKED(304, "用户被锁定"),
    LOGIN_FAIL_ACCOUNT_DISABLED(305, "用户未启用"),
    LOGIN_FAIL_ACCOUNT_EXPIRED(306, "用户过期"),
    LOGIN_FAIL_ACCOUNT_UNKNOWN(307, "不存在该用户"),
    LOGIN_FAIL_INCORRECT_CREDENTIALS(308, "密码不正确"),
    LOGIN_FAIL_CAPTCHA_ERROR(309, "验证码错误"),
    PASSWORD_NOT_MATCH(310, "密码不匹配"),
    BAD_REQUEST(400, "请求参数出错"),
    UNLOGIN(401, "没有登录"),
    UNAUTHORIZED(403, "没有权限"),
    NO_SUPPORTED_MEDIATYPE(415, "不支持的媒体类型,请使用application/json;charset=UTF-8"),
    VALIDATE_CODE_NOT_FOUND(478, "验证码不翼而飞"),
    INTERNAL_SERVER_ERROR(500, "服务器出错"),
    DATA_DUPLICATE_KEY(601, "数据重复"),
    SMS_SYSTEM_ERROR(701, "短信平台系统异常"),
    SMS_SYSTEM_EXPIRED(702, "短信验证码已过期");

    private final int value;
    private final String message;

    ResultCodeEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int value() {
        return this.value;
    }

    public String message() {
        return this.message;
    }

}
