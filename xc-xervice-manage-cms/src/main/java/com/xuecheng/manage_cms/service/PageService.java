package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manage_cms.client.CourseClient;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepostry;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;



/*
 * @author Wang
 * @date 2019/8/17 16:00
 */


@Service
public class PageService {

    @Autowired
    CmsPageRepostry cmsPageRepostry;

    @Autowired
    ConfigService configService;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    CourseClient courseClient;

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

      /*  if (!StringUtils.isEmpty(queryPageRequest.getPageName())) {
            cmsPage.setPageName(queryPageRequest.getPageName());
            exampleMatcher.withMatcher(queryPageRequest.getPageName(),ExampleMatcher.GenericPropertyMatchers.contains());
//            exampleMatcher = exampleMatcher.withMatcher(queryPageRequest.getPageAliase(), ExampleMatcher.GenericPropertyMatchers.contains());
        }*/

        //设置条件值
        if (!StringUtils.isEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if (!StringUtils.isEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if (!StringUtils.isEmpty(queryPageRequest.getPageName())) {
            cmsPage.setPageName(queryPageRequest.getPageName());
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
     *
     * @return
     */
    public QueryResponseResult findAll() {
        List<CmsSite> siteId = new ArrayList<>();

        String newsiteId = "";

        List<CmsPage> all = cmsPageRepostry.findAll();

        CmsSite cmsSite = new CmsSite();

        for (CmsPage alls : all) {
            String siteid = alls.getSiteId();
//            newsiteId = siteid;
            if (newsiteId != siteid) {
                newsiteId = siteid;
            }
        }
        cmsSite.setSiteId(newsiteId);
        cmsSite.setSiteName("门户主站");

        siteId.add(cmsSite);

        QueryResult queryResult = new QueryResult();
        queryResult.setList(siteId);
        queryResult.setTotal(siteId.size());

        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);

        return queryResponseResult;
    }

    /**
     * 新增页面
     *
     * @param cmsPage
     * @return
     */
    public CmsPageResult addCmsPage(CmsPage cmsPage) {
        if (cmsPage == null){
            //抛出异常，非法参数异常，指定异常信息的内容
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //校验页面名称、站点id、页面webpath的唯一性
        //根据页面名称、站点id、页面webpath去cms_page集合，如果查到证明此页面已经存在，如果查不到则进行添加。
        String pageName = cmsPage.getPageName();
        String siteId = cmsPage.getSiteId();
        String pageWebPath = cmsPage.getPageWebPath();
        CmsPage byPageNameAndSiteIdAndPageWebPath = cmsPageRepostry.findByPageNameAndSiteIdAndPageWebPath(pageName, siteId, pageWebPath);
        if (byPageNameAndSiteIdAndPageWebPath != null) {
            //页面已经存在
            //抛出异常，异常内容就是页面已经存在
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        //调用dao新增页面
        cmsPage.setPageId(null);
        CmsPage saveCmsPage = cmsPageRepostry.save(cmsPage);
        CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        return cmsPageResult;
    }

    /**
     * 根据页面id查询页面
     *
     * @param id
     * @return
     */
    public CmsPage getById(String id) {
        Optional<CmsPage> optional = cmsPageRepostry.findById(id);
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            return cmsPage;
        }

        return null;
    }



    /**
     * 修改页面
     *
     * @param id
     * @param cmsPage
     * @return
     */
    public CmsPageResult updatePage(String id, CmsPage cmsPage) {
        CmsPage byId = this.getById(id);
        if (!StringUtils.isEmpty(byId)) {

            byId.setSiteId(cmsPage.getSiteId());
            byId.setPageAliase(cmsPage.getPageAliase());
            byId.setTemplateId(cmsPage.getTemplateId());
            byId.setPageName(cmsPage.getPageName());
            byId.setPageWebPath(cmsPage.getPageWebPath());
            byId.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            byId.setDataUrl(cmsPage.getDataUrl());

            // byId.setPageCreateTime(cmsPage.getPageCreateTime());
            // byId.setPageType(cmsPage.getPageType());
            //提交修改
            //如果页面名称、站点id、页面webpath已经存在，说明已经有这样的页面，无法修改
            CmsPage save = cmsPageRepostry.save(byId);
            CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, save);
            return  cmsPageResult;
        }
             //修改失败
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    /**
     * 根据页面id删除页面
     * @param id
     * @return
     */
    public ResponseResult deleteCmsPage(String id){
        Optional<CmsPage> optional = cmsPageRepostry.findById(id);
        if (optional.isPresent()){
            cmsPageRepostry.deleteById(id);
            ResponseResult responseResult = new ResponseResult(CommonCode.SUCCESS);
            return responseResult;
        }
       return new ResponseResult(CommonCode.FAIL);
    }

    public ResponseResult post(String pageId){
//1.执行页面静态化
        String pageHtml = configService.getPageHtml(pageId);
        // 2.将页面静态化文件存储到GridFs中
        CmsPage cmsPage = savaHtml(pageId, pageHtml);
        //3.向MQ发送消息
        sendPostPage(pageId);

        return new ResponseResult(CommonCode.SUCCESS);

    }

    //保存静态文件内容
    private CmsPage savaHtml(String pageId , String html){
        //1.查询页面
        Optional<CmsPage> optional = cmsPageRepostry.findById(pageId);
        if (!optional.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = optional.get();
        //2.存之前先删除页面
        String fileId = cmsPage.getHtmlFileId();
        if (StringUtils.isEmpty(fileId)){

        }else {
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(fileId)));
        }
        //3.保存html文件到gridfs
        InputStream inputStream = null;
        ObjectId objectId = null;
        try {
            inputStream = IOUtils.toInputStream(html,"utf-8");
            objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //4.将文件存储到cmspage中
        cmsPage.setHtmlFileId(objectId.toHexString());
        cmsPageRepostry.save(cmsPage);
        return cmsPage;
    }

    //向mq发送消息
    private void sendPostPage(String pageId){

        //得到页面信息
        Optional<CmsPage> optional = cmsPageRepostry.findById(pageId);
        if (!optional.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = optional.get();

        //获得站点id，即routingKey
        String siteId = cmsPage.getSiteId();

        //拼装消息对象
        Map<String,String> map = new HashMap<>();
        map.put("pageId",pageId);
        //转成json串
        String jsonString = JSON.toJSONString(map);
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,siteId,jsonString);
    }

    public CmsPageResult saveCmsPage(CmsPage cmsPage) {

        CmsPage cmsPage1 = cmsPageRepostry.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (cmsPage1 != null){
            //修改
            CmsPageResult cmsPageResult = this.updatePage(cmsPage1.getPageId(), cmsPage);
            return cmsPageResult;
        }
        CmsPage save = cmsPageRepostry.save(cmsPage);
        CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        return cmsPageResult;
    }
}