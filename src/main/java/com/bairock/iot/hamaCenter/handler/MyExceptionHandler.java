package com.bairock.iot.hamaCenter.handler;

import com.bairock.iot.hamaCenter.exception.MyException;
import com.bairock.iot.hamaCenter.utils.Result;
import com.bairock.iot.hamaCenter.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常处理
 */
@ControllerAdvice
public class MyExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler
    @ResponseBody
    public Result<?> handler(Exception exception, HttpServletResponse response){

        if(exception instanceof MyException) {
            MyException myException = (MyException) exception;
            return ResultUtil.error(myException.getResultEnum());
        }else if(exception instanceof NullPointerException){
            exception.printStackTrace();
            StringBuilder message = new StringBuilder(exception.toString());
            message.append('\n');
            StackTraceElement[] stackTraceElements = exception.getStackTrace();
            if(stackTraceElements.length > 0) {
                for(StackTraceElement s : stackTraceElements){
                    if(s.getClassName().contains("com.bairock")){
                        message.append(s.getFileName()).append("-").append(s.getMethodName()).append("-").append(s.getLineNumber());
                        message.append('\n');
                    }
                }
            }
            logger.error(exception.getMessage());
            return ResultUtil.error(-1, message.toString());
        }else{
            exception.printStackTrace();
            logger.error(exception.getMessage());
            return ResultUtil.error(-1, exception.getMessage());
        }
    }

//    @ResponseBody
//    @ExceptionHandler(value = UserPrincipalNullException.class)
//    public Result handleUserPrincipalNullException(UserPrincipalNullException e, HttpServletResponse response) {
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        return ResultUtil.error(ResultEnum.AUTHENTICATION_FAIL);
//    }
}
