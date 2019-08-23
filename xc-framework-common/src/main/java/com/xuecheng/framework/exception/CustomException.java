package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 自定义异常类型
 * @author Wang
 * @date 2019/8/23 9:21
 */

public class CustomException extends RuntimeException{

    //错误代码
    ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        this.resultCode=resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
