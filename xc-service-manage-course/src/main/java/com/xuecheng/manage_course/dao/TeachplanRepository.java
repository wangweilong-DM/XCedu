package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/8/31 14:15
 */
public interface TeachplanRepository extends JpaRepository<Teachplan,String> {

    public List<Teachplan> findByCourseidAndParentid(String courseId,String parentId);

    public List<Teachplan> findByCourseid(String courseId);
}
