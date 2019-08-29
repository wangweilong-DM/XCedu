package com.xuecheng.manage_cms_client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.dao.CmsPageRepostry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/8/28 10:02
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class testRabbit {

    @Autowired
    CmsPageRepostry cmsPageRepostry;

    @Test
    public void findByPageId(){
        String pageId = "5a754adf6abb500ad05688d9";
        CmsPage cmsPage = cmsPageRepostry.findByPageId(pageId);
        System.out.println(cmsPage);
    }
}
