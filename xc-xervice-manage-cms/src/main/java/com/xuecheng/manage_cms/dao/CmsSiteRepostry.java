package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/8/29 9:21
 */
public interface CmsSiteRepostry extends MongoRepository<CmsSite,String>{


}
