package com.bairock.iot.hamaCenter.utils;

public enum ResultEnum {

    UNKNOWN(-1, "未知"),
    SUCCESS(200, "SUCCESS"),
    NO_ENTITY(1, "无对应数据"),
    INSERT_NULL_OBJECT(2, "插入对象为空"),
    PROPERTY_NULL(3, "关键字段为空"),
    ENUM_VALUE_ERROR(4, "枚举值错误"),
    PARM_ERROR(5, "参数错误"),
    FILE_SAVE_ERROR(6, "文件保存错误"),
    PASSWORD_ERROR(7, "密码错误"),
    USER_LOCKED(8, "账号已冻结"),
    NO_USER(9, "用户不存在"),
    USERNAME_REGISTERED(10, "用户名已注册"),
    AUTHORITY_NAME_REPEAT(11, "权限名重复"),
    AUTHORITY_PARENT_NULL(12, "父权限不存在"),
    AUTHENTICATION_FAIL(401, "认证失败"),
    LOGOUT_SUCCESS(401, "退出成功"),
    TOKEN_NULL(401, "Token不存在"),
    TOKEN_FORMAT_ERR(15, "Token格式不正确 没有userId"),
    SYSTEM_RETAIN(16, "系统保留"),
    CODE_EXISTS(17, "码值重复"),
    IMPORT_FILE_EMPTY(18, "上传文件为空"),
    CONFIG_ID_ERROR(19, "配置项id不正确"),
    FILE_EMPTY(20, "文件为空"),
    ID_NULL(21, "没有id"),
    CHAIN_UNABLE(22, "外链链接已失效"),
    DISPATCH_MATCH_ERR(23, "收文匹配失败"),
    OSS_DELETE_ERR(24, "OSS文件删除失败");

    private int code;
    private String message;

    ResultEnum(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
