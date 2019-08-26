package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Wang
 * @date 2019/8/25 19:51
 */
public interface CmsConfigRepostry extends MongoRepository<CmsConfig,String> {
}
