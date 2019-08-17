package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * @author Wang
 * @date 2019/8/17 13:13
 */
public interface CmsPageControllerApi {

    //页面查询
    public QueryResponseResult findList(int page,int size, QueryResponseResult queryResponseResult);

}
