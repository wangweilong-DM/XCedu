package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Wang
 * @date 2019/8/26 16:01
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String >{

}
