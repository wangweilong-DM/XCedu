package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsConfigControllerApi;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wang
 * @date 2019/8/25 19:48
 */
@RestController
@RequestMapping("cms/config")
public class CmsConfigController implements CmsConfigControllerApi{

    @Autowired
    ConfigService configService;
    public CmsConfig findById(@PathVariable String id){
        return configService.fingById(id);
    }

    @Override
    @GetMapping("/getmodel/{id}")
    public CmsConfig getModel(@PathVariable("id") String id) {
        return configService.fingById(id);
    }
}
