package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/9/3 8:41
 */
public interface SysDictionaryRepostry extends MongoRepository<SysDictionary,String> {

    public SysDictionary findByDType(String type);
}
