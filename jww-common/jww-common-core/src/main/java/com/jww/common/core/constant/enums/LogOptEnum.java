package com.jww.common.core.constant.enums;

/**
 * 日志操作类型枚举
 *
 * @author wanyong
 * @date 2018-10-31 16:16
 */
public enum LogOptEnum {

    // 查询
    QUERY(0, "查询"),
    ADD(1, "新增"),
    MODIFY(2, "修改"),
    DELETE(3, "删除"),
    LOGIN(4, "登录"),
    UNKNOW(9, "未知");
    private final int value;
    private final String message;

    LogOptEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int value() {
        return this.value;
    }

    public String getMessage() {
        return this.message;
    }

}
