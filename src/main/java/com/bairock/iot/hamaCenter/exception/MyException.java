package com.bairock.iot.hamaCenter.exception;

import com.bairock.iot.hamaCenter.utils.ResultEnum;

/**
 * 自定义异常
 */
public class MyException extends RuntimeException {

    private ResultEnum resultEnum;

    public MyException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.resultEnum = resultEnum;
    }

    public MyException(String message) {
        super(message);
        this.resultEnum = ResultEnum.UNKNOWN;
        this.resultEnum.setMessage(message);
    }

    public MyException(ResultEnum resultEnum, String message) {
        super(message);
        this.resultEnum = resultEnum;
        this.resultEnum.setMessage(message);
    }

    public ResultEnum getResultEnum() {
        return resultEnum;
    }

    public void setResultEnum(ResultEnum resultEnum) {
        this.resultEnum = resultEnum;
    }
}
