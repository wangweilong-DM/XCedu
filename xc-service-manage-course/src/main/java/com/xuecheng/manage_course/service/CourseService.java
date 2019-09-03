package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/8/31 9:07
 */
@Service
public class CourseService {


    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    CourseBaseRepository courseBaseRepository;

    @Autowired
    TeachplanRepository teachplanRepository;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 课程计划查询
     *
     * @param courseId 课程id
     * @return
     */
    public TeachplanNode fingTeachPlanList(String courseId) {
        if (StringUtils.isEmpty(courseId)) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);

        if (teachplanNode == null) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        return teachplanNode;

    }

    /**
     * 添加课程计划
     *
     * @param teachplan
     * @return
     */
    public ResponseResult addTeachplan(Teachplan teachplan) {

        if (teachplan == null ||
                StringUtils.isEmpty(teachplan.getCourseid()) ||
                StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }

        //课程id
        String courseid = teachplan.getCourseid();

        //父节点id
        String parentid = teachplan.getParentid();

        if (StringUtils.isEmpty(parentid)) {
            //如果父节点为空则取出根节点
            //  parentid = this.getTeachplanRoot(courseid);
        }
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        //父节点
        Teachplan teachplanParent = optional.get();
        String parentGrade = teachplanParent.getGrade();
        //设置父节点
        teachplan.setParentid(parentid);
        teachplan.setStatus("0");//未发布

        //子节点的级别，根据父节点来判断
        if (parentGrade.equals("1")) {
            teachplan.setGrade("2");
        }
        if (parentGrade.equals("2")) {
            teachplan.setGrade("3");
        }
        //设置课程id
        teachplan.setCourseid(courseid);
        teachplanRepository.save(teachplan);

        ResponseResult responseResult = new ResponseResult(CommonCode.SUCCESS);
        return responseResult;
    }

    //添加课程根节点，如果没有则手动配置
    private String getTeachplanRoot(String courseId) {
        //校验根节点
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            return null;
        }
        CourseBase courseBase = optional.get();
        //取出课程计划根节点
        List<Teachplan> list = teachplanRepository.findByCourseidAndParentid(courseId, "0");


        if (list == null) {
            //新增一个根节点
            Teachplan teachplan = new Teachplan();
            teachplan.setCourseid(courseId);
            teachplan.setPname(courseBase.getName());
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setStatus("0");
            teachplanRepository.save(teachplan);
            return teachplan.getId();

        }

        Teachplan teachplan = list.get(0);
        return teachplan.getId();
    }

    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest) {
        PageHelper.startPage(page, size);
        Page<CourseBase> courseList = courseMapper.findCourseList();
        List<CourseBase> result = courseList.getResult();
        QueryResult queryResult = new QueryResult();
        queryResult.setList(result);
//        queryResult.setTotal();
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);

        return queryResponseResult;

    }

    //级联选择器查询课程分类
    public CategoryNode findCategoryList() {

        CategoryNode categoryList = categoryMapper.findCategoryList();


        return categoryList;
    }

    @Transactional
    public AddCourseResult addCourseBase(CourseBase courseBase) {

        courseBase.setStatus("202001");
        CourseBase save = courseBaseRepository.save(courseBase);
        AddCourseResult addCourseResult = new AddCourseResult(CommonCode.SUCCESS, courseBase.getId());
        return addCourseResult;
    }

    @Transactional
    public CourseBase getCourseBaseById(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        CourseBase courseBase = optional.get();
        return courseBase;
    }

    public ResponseResult updateCourseBase(String id, CourseBase courseBase) {

       /* if (id == null || courseBase == null) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }*/
        Optional<CourseBase> optional = courseBaseRepository.findById(id);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }

        CourseBase courseBase1 = optional.get();
        courseBase1.setStatus(courseBase.getStatus());
        courseBase1.setDescription(courseBase.getDescription());
        courseBase1.setGrade(courseBase.getGrade());
        courseBase1.setName(courseBase.getName());
        courseBase1.setUsers(courseBase.getUsers());
        courseBase1.setSt(courseBase.getSt());
        courseBase1.setStudymodel(courseBase.getStudymodel());

        CourseBase save = courseBaseRepository.save(courseBase1);

        ResponseResult responseResult = new ResponseResult(CommonCode.SUCCESS);

        return responseResult;
    }
}
