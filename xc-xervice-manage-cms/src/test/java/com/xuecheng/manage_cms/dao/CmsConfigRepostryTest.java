package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * @author Wang
 * @date 2019/8/25 19:49
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsConfigRepostryTest {

    @Autowired
    CmsConfigRepostry cmsConfigRepostry;

    @Test
    public void testFindById(){
        Optional<CmsConfig> optional = cmsConfigRepostry.findById("5a791725dd573c3574ee333f");
        if (optional.isPresent()){
            CmsConfig cmsConfig = optional.get();
            System.out.println(cmsConfig);
        }

    }
}
