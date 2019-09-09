package com.xuecheng.manage_cms.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/9/9 9:39
 */
@FeignClient(value = XcServiceList.XC_SERVICE_CMS)
public interface CmsPageClient {

    @GetMapping("/cms/config/getmodel/{id}")
    public CmsConfig getModel(@PathVariable("id") String id);
}
