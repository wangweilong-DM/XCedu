package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/9/2 13:43
 */
@Api(value = "课程分类管理" ,description = "课程分类管理"/* ,tags = {"课程分类管理"}*/)
public interface CategoryControllerApi {
    @ApiOperation("查询课程分类")
    public CategoryNode findList();
}
