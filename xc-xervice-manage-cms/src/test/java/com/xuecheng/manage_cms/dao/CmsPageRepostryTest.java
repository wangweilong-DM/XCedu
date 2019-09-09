package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsConfigModel;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms.service.ConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Wang
 * @date 2019/8/17 15:04
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepostryTest {

    @Autowired
    CmsConfigRepostry cmsConfigRepostry;

    @Autowired
    CmsPageRepostry cmsPageRepostry;

    @Autowired
    ConfigService configService;

    @Autowired
    CmsSiteRepostry cmsSiteRepostry;

    @Test
    public void testFindAll(){

        List all = cmsPageRepostry.findAll();
        System.out.println(all);
    }

    @Test
    public void testFindAllByPage(){
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepostry.findAll(pageable);

        System.out.println(all);
    }

    @Test
    public void testUpadte(){
        Optional<CmsPage> byId = cmsPageRepostry.findById("5a795ac7dd573c04508f3a56");
        if (byId.isPresent()){
            CmsPage cmsPage = byId.get();
            cmsPage.setPageAliase("test001");
            Object save = cmsPageRepostry.save(cmsPage);
        }
    }

    @Test
    public void testfindByalise(){
        CmsPage test = cmsPageRepostry.findByPageAliase("test001");
        System.out.println(test);
    }


    @Test
    public void testfindAll(){

        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);

        CmsPage cmsPage = new CmsPage();
//        cmsPage.setPageName("preview_297e7c7c62b888f00162b8a7dec20000.html");
        cmsPage.setPageAliase("t");
        ExampleMatcher exampleMatch = ExampleMatcher.matching();

        exampleMatch = exampleMatch.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<CmsPage> example = Example.of(cmsPage,exampleMatch);
        Page<CmsPage> all = cmsPageRepostry.findAll(example, pageable);
        List<CmsPage> content = all.getContent();
        System.out.println(content);
    }

    @Test
    public void testfindBySiteId(){
       List< CmsPage> siteId = cmsPageRepostry.findBySiteId("5a751fab6abb5044e0d19ea1");
       System.out.println(siteId);

    }

    @Test
    public void testfideBy(){
        CmsPage cmsPage = cmsPageRepostry.findByPageNameAndSiteIdAndPageWebPath(
                "index.html", "5a751fab6abb5044e0d19ea1", "/index.html");
        System.out.println(cmsPage);
    }

    @Test
    public  void testfindByid1(){
        Optional<CmsPage> optional = cmsPageRepostry.findById("5a754adf6abb500ad05688d9");
        if (!StringUtils.isEmpty(optional)){
            CmsPage cmsPage = optional.get();
            System.out.println(cmsPage);
        }

    }

    @Test
    public void testupdate(){
        CmsPage cmsPage =new CmsPage();
        Optional<CmsPage> optional = cmsPageRepostry.findById("5d5cd6aba8d3fb04241db8e3");
        if (!StringUtils.isEmpty(optional)){
            cmsPage = optional.get();
        }
        cmsPage.setPageName("ttt");
        CmsPage save = cmsPageRepostry.save(cmsPage);
        System.out.println(save);
    }

    @Test
    public void testGetPageHtml(){
        String pageHtml = configService.getPageHtml("5d5c93bca8d3fb36dcd604e2");
        System.out.println(pageHtml);
    }

    @Test
    public void  testSide(){
        Optional<CmsSite> optional = cmsSiteRepostry.findById("5a751fab6abb5044e0d19ea1");
        if (optional.isPresent()){
            CmsSite cmsSite = optional.get();
            cmsSite.setSitePhysicalPath("cmsfiles");
            CmsSite save = cmsSiteRepostry.save(cmsSite);
        }
    }
    @Test
    public  void  test(){
        Optional<CmsConfig> optional = cmsConfigRepostry.findById("5a791725dd573c3574ee333f");
        CmsConfig cmsConfig = optional.get();
        List model =cmsConfig.getModel();
        System.out.println(model);
    }
}
