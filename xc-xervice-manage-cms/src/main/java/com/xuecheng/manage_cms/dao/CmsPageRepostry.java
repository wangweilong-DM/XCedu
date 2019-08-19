package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Wang
 * @date 2019/8/17 15:00
 */
public interface CmsPageRepostry extends MongoRepository<CmsPage,String>{

//    public QueryResponseResult findList(int page,int size, QueryResponseResult queryResponseResult);

     //根据别名查询
     CmsPage findByPageAliase(String aliase);
}
