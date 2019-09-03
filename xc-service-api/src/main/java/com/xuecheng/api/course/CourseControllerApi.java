package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/8/30 14:57
 */
@Api(value = "课程管理接口", description = "课程管理接口，提供课程增删改查")
public interface CourseControllerApi{


    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("查询我的课程")
    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest);

    @ApiOperation("新增我的课程")
    public AddCourseResult addCourseBase(CourseBase courseBase);

    @ApiOperation("根据课程ID查询课程基础信息")
    public CourseBase getCourseBaseById(String courseId);

    @ApiOperation("修改课程信息")
    public ResponseResult updateCourseBase(String id,CourseBase courseBase);

    @ApiOperation("根据id获取课程营销信息")
    public CourseMarket getCourseMarketById(String courseId);

    @ApiOperation("修改课程营销信息")
    public ResponseResult updateCourseMarket(String courseId, CourseMarket courseMarket);
}
