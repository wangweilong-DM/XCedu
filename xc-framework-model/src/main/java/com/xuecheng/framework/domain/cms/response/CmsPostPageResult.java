package com.xuecheng.framework.domain.cms.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/9/11 13:41
 */
@Data
@NoArgsConstructor
public class CmsPostPageResult extends ResponseResult {

    String pageUrl;
    public CmsPostPageResult(ResultCode resultCode, String pageUrl){
        super(resultCode);
        this.pageUrl = pageUrl;
    }
}
