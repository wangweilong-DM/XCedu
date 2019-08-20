package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepostry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;



/*
 * @author Wang
 * @date 2019/8/17 16:00
 */


@Service
public class PageService {

    @Autowired
    CmsPageRepostry cmsPageRepostry;

    /**
     * 自定义查询页面方法
     *
     * @param page             当前页
     * @param size             每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {


        //自定义条件查询
        //定义条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        //条件值对象
        CmsPage cmsPage = new CmsPage();
        if (!StringUtils.isEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
            exampleMatcher = exampleMatcher.withMatcher(queryPageRequest.getPageAliase(), ExampleMatcher.GenericPropertyMatchers.contains());
        }

            //设置条件值
            if (!StringUtils.isEmpty(queryPageRequest.getSiteId())) {
                cmsPage.setSiteId(queryPageRequest.getSiteId());
            }
            if (!StringUtils.isEmpty(queryPageRequest.getTemplateId())) {
                cmsPage.setTemplateId(queryPageRequest.getTemplateId());
            }

        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);


        //分页参数
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 1;
        }
        page = page - 1;


        Pageable pageable = PageRequest.of(page, size);
        // Page<CmsPage> all = cmsPageRepostry.findAll(pageable);
        Page<CmsPage> all = cmsPageRepostry.findAll(example, pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());    //数据列表
        queryResult.setTotal(all.getTotalElements());  //数据总记录数

        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    /**
     * 查询所有siteId,根据siteID进行分组
     * @return
     */
    public QueryResponseResult findAll(){
        List<CmsSite> siteId = new ArrayList<>();

        String newsiteId = "";

        List<CmsPage> all = cmsPageRepostry.findAll();

        CmsSite cmsSite = new CmsSite();

        for (CmsPage alls:all){
            String siteid = alls.getSiteId();
//            newsiteId = siteid;
            if (newsiteId != siteid){
                newsiteId = siteid;

            }

        }
        cmsSite.setSiteId(newsiteId);
        cmsSite.setSiteName("门户主站");

        siteId.add(cmsSite);

        QueryResult queryResult = new QueryResult();
        queryResult.setList(siteId);
        queryResult.setTotal(siteId.size());

        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);

        return queryResponseResult;
    }

    /**
     * 新增页面
     * @param cmsPage
     * @return
     */
    public CmsPageResult addCmsPage(CmsPage cmsPage){
        //校验页面名称、站点id、页面webpath的唯一性
        //根据页面名称、站点id、页面webpath去cms_page集合，如果查到证明此页面已经存在，如果查不到则进行添加。
        String pageName = cmsPage.getPageName();
        String siteId = cmsPage.getSiteId();
        String pageWebPath = cmsPage.getPageWebPath();
        CmsPage byPageNameAndSiteIdAndPageWebPath = cmsPageRepostry.findByPageNameAndSiteIdAndPageWebPath(pageName, siteId, pageWebPath);
        if (byPageNameAndSiteIdAndPageWebPath != null){
            //调用dao新增页面
            CmsPage saveCmsPage = cmsPageRepostry.save(cmsPage);
        }




        //CmsPageResult cmsPageResult = new CmsPageResult();
        return null;
    }

}
