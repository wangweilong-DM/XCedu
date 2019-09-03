package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/9/2 14:02
 */
@Mapper
public interface CategoryMapper  {
    CategoryNode findCategoryList();
}
