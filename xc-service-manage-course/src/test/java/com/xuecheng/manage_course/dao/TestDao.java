package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.Soundbank;
import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;

    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanRepository teachplanRepository;

    @Autowired
    CategoryMapper categoryMapper;
    @Test
    public void testCourseBaseRepository(){
        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if(optional.isPresent()){
            CourseBase courseBase = optional.get();
            System.out.println(courseBase);
        }

    }

    @Test
    public void testCourseMapper(){
        CourseBase courseBase = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        System.out.println(courseBase);

    }

    @Test
    public void testQuery(){
        String courseId = "4028e581617f945f01617f9dabc40000";
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        System.out.println(teachplanNode);
    }

    @Test
    public void testFindByCourseId(){
        List<Teachplan> list = teachplanRepository.findByCourseidAndParentid("4028e581617f945f01617f9dabc40000","0");
        System.out.println(list);
    }

    @Test
    public void testSaveTeachplan(){
        Teachplan teachplan = new Teachplan();
        teachplan.setCourseid("4028e581617f945f01617f9dabc40000");
        teachplan.setParentid("0");
        teachplan.setPname("java课程设计");
        teachplan.setGrade("1");
        teachplan.setStatus("0");
        Teachplan save = teachplanRepository.save(teachplan);
    }

    @Test
    public void testfindByPage(){

       // CourseBase courseBaseById = courseMapper.findCourseBaseById("297e7c7c62b888f00162b8a7dec20000");
       // System.out.println(courseBaseById);

        PageHelper.startPage(1,5);  //查询第一页，每页显示10条数据

     //   CourseListRequest courseListRequest = new CourseListRequest();
        Page<CourseBase> courseList = courseMapper.findCourseList();
        List<CourseBase> result = courseList.getResult();
        System.out.println(result);
    }

    @Test
    public void testFindList(){
        CategoryNode categoryList = categoryMapper.findCategoryList();

        System.out.println(categoryList);
    }

    @Test
    public void testAdd(){
        CourseBase courseBase = new CourseBase();
        courseBase.setCompanyId("sssss");
        courseBase.setDescription("String");
        CourseBase save = courseBaseRepository.save(courseBase);
        System.out.println(save);
    }

}
