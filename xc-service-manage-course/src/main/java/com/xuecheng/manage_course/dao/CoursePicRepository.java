package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/9/6 14:20
 */

public interface CoursePicRepository extends JpaRepository<CoursePic,String>{

    long deleteByCourseid(String courseId);
}
