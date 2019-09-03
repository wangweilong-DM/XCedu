package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseMarket;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/9/3 15:27
 */
@Mapper
public interface CourseMarketMapper {
     CourseMarket findById(String id);
}
