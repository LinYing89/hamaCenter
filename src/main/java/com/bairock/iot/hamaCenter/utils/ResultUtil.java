package com.bairock.iot.hamaCenter.utils;

import java.util.Collection;

public class ResultUtil {

    public static Result<?> success(Object o){
        Result<Object> result = new Result<>();
        result.setStatus(ResultEnum.SUCCESS.getCode());
        result.setMessage(ResultEnum.SUCCESS.getMessage());
        if(o instanceof Collection){
            result.setCount(((Collection<?>)o).size());
        }
        result.setData(o);
        return result;
    }

    public static Result<?> success(){
        Result<String> result = new Result<>();
        result.setStatus(ResultEnum.SUCCESS.getCode());
        result.setMessage(ResultEnum.SUCCESS.getMessage());
        result.setData("OK");
        return result;
    }

    public static Result<?> error(ResultEnum resultEnum){
        Result<?> result = new Result<>();
        result.setStatus(resultEnum.getCode());
        result.setMessage(resultEnum.getMessage());
        return result;
    }

    public static Result<?> error(int code, String message){
        Result<?> result = new Result<>();
        result.setStatus(code);
        result.setMessage(message);
        return result;
    }

    public static Result<?> error(String message, Object object){
        Result<Object> result = new Result<>();
        result.setStatus(-1);
        result.setMessage(message);
        result.setData(object);
        return result;
    }
}
