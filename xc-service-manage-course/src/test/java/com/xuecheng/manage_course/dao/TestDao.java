package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
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
}
