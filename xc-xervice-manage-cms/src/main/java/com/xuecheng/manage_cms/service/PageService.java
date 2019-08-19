package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepostry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



/*
 * @author Wang
 * @date 2019/8/17 16:00
 */


@Service
public class PageService {

    @Autowired
    CmsPageRepostry cmsPageRepostry;

    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (page <= 0 ){
            page = 1;
        }
        if (size <= 0){
            size = 1;
        }
        page = page - 1;
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepostry.findAll(pageable);

        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());    //数据列表
        queryResult.setTotal(all.getTotalElements());  //数据总记录数

        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }
}
