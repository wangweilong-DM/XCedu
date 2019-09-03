package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.SysDicthinaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/9/3 8:33
 */
@RestController
@RequestMapping("sys")
public class CmsDictionaryController implements SysDicthinaryControllerApi {
    @Autowired
    ConfigService configService;

    @Override
    @GetMapping("/dictionary/get/{dtype}")
    public SysDictionary getByType(@PathVariable("dtype") String type) {
        return configService.getByType(type);
    }
}
