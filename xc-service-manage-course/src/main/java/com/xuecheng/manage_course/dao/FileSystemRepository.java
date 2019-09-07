package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/9/5 13:57
 */
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {

}
