package com.xuecheng.manage_course.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.course.ext.CourseView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/9/9 20:37
 */
@FeignClient(value = XcServiceList.XC_SERVICE_COURSE)
public interface CourseClient {

    @GetMapping("/course/courseview/{courseId}")
    public CourseView courseView(String courseId);

}
