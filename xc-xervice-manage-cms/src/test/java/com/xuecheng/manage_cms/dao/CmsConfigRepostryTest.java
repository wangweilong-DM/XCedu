package com.xuecheng.manage_cms.dao;

import com.google.common.collect.Maps;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsConfigModel;
import com.xuecheng.framework.domain.cms.SysDictionary;
import com.xuecheng.manage_cms.client.CmsPageClient;
import org.apache.commons.collections.BeanMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;

/**
 * @author Wang
 * @date 2019/8/25 19:49
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsConfigRepostryTest {
    @Autowired
    SysDictionaryRepostry sysDictionaryRepostry;

    @Autowired
    CmsConfigRepostry cmsConfigRepostry;

    @Autowired
    CmsPageClient cmsPageClient;

    @Test
    public void testFindById(){
        Optional<CmsConfig> optional = cmsConfigRepostry.findById("5a791725dd573c3574ee333f");
        if (optional.isPresent()){
            CmsConfig cmsConfig = optional.get();
            System.out.println(cmsConfig);
        }

    }

    @Test
    public void testFindByType(){
        com.xuecheng.framework.domain.system.SysDictionary byDType = sysDictionaryRepostry.findByDType("100");
        System.out.println(byDType);

    }

    @Test
    public  void  test(){
        CmsConfig model = cmsPageClient.getModel("5a791725dd573c3574ee333f");
        System.out.println(model);

            if(model != null) {

                Map map = new BeanMap(model);
                System.out.println(map);
            }
        SortedMap<String, String> signMaps = Maps.newTreeMap();
        org.springframework.cglib.beans.BeanMap beanMap = org.springframework.cglib.beans.BeanMap.create(model);
        for (Object key : beanMap.keySet()) {
            Object value = beanMap.get(key);

            // 排除空数据
            if (value == null) {
                continue;
            }

            signMaps.put(key + "", String.valueOf(value));

            System.out.println(signMaps);
        }
    }
}
