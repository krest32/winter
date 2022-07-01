package com.krest.utils.myexception;

import com.krest.utils.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ControllerAdvice 统一处理的异常注解
 */

@Slf4j
@ControllerAdvice
public class globalExceptionHandler {


    /**
     * 异常以Jason的形式传递，所以使用
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public R error(Exception e) {
        e.printStackTrace();
        String message = e.getMessage();
        return R.error().message("服务器：程序发生异常");
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("服务器：执行了特定异常");
    }


    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R error(MethodArgumentNotValidException e){
        e.printStackTrace();
        return R.error().message("服务器：数据校验异常");
    }


    @ExceptionHandler()
    @ResponseBody
    public R error(myException e){
        e.printStackTrace();
        log.error(exceptionUtils.getMessage(e));
        return R.error().message(e.getMsg()).code(e.getCode());

    }

}
