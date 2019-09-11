package com.xuecheng.framework.domain.course.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/9/10 19:46
 */
@Data
@ToString
@NoArgsConstructor
public class CoursePublishResult extends ResponseResult{
    String previewUrl;  //页面预览url，必须得到页面id才可以拼装
    public  CoursePublishResult(ResultCode resultCode,String previewUrl){
        super(resultCode);
        this.previewUrl= previewUrl;
    }

}
