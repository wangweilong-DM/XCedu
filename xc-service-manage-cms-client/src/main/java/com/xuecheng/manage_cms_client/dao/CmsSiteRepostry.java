package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/8/28 10:44
 */
public interface CmsSiteRepostry extends MongoRepository<CmsSite,String> {
}
