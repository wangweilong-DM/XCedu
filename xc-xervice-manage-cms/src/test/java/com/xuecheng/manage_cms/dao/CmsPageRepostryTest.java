package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * @author Wang
 * @date 2019/8/17 15:04
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepostryTest {

    @Autowired
    MongoRepository mongoRepository;

    @Autowired
    CmsPageRepostry cmsPageRepostry;

    @Test
    public void testFindAll(){

        List all = mongoRepository.findAll();
        System.out.println(all);
    }

    @Test
    public void testFindAllByPage(){
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = mongoRepository.findAll(pageable);

        System.out.println(all);
    }

    @Test
    public void testUpadte(){
        Optional<CmsPage> byId = mongoRepository.findById("5a795ac7dd573c04508f3a56");
        if (byId.isPresent()){
            CmsPage cmsPage = byId.get();
            cmsPage.setPageAliase("test001");
            Object save = mongoRepository.save(cmsPage);
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


}
