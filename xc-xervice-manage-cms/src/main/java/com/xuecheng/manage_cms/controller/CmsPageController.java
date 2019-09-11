package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Wang
 * @date 2019/8/17 14:29
 */
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {
    @Autowired
    PageService pageService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        // QueryResponseResult queryResponseResult1 = new QueryResponseResult(CommonCode.SUCCESS);

        return pageService.findList(page, size, queryPageRequest);
    }

    @Override
    @GetMapping("/list/query")
    public QueryResponseResult findAll() {

        return pageService.findAll();
    }

    @Override
    @PostMapping("add")
    public CmsPageResult addCmsPage(@RequestBody CmsPage cmsPage) {
        return pageService.addCmsPage(cmsPage);
    }

    @Override
    @GetMapping("get/{id}")
    public CmsPage findByid(@PathVariable("id") String id) {
        return pageService.getById(id);
    }

    @Override
    @PutMapping("put/{id}")//这里使用put方法，http方法中put表示更新
    public CmsPageResult edit(@PathVariable("id") String id,@RequestBody CmsPage cmsPage) {
        return pageService.updatePage(id,cmsPage);
    }

    @Override
    @DeleteMapping("delete/{id}")
    public ResponseResult delete(@PathVariable String id) {
        return pageService.deleteCmsPage(id);
    }

    @Override
    @PostMapping("/postPage/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return pageService.post(pageId);
    }

    @Override
    @PostMapping("/save")
    public CmsPageResult saveCmsPage(@RequestBody CmsPage cmsPage) {
        return pageService.saveCmsPage(cmsPage);
    }

}
