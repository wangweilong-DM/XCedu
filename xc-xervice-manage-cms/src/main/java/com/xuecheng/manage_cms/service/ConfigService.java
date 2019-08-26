package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.dao.CmsConfigRepostry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author Wang
 * @date 2019/8/25 20:00
 */
@Service
public class ConfigService {

    @Autowired
    CmsConfigRepostry cmsConfigRepostry;


    /**
     * 根据id查询cms页面配置
     * @param id
     * @return
     */
    public CmsConfig fingById(String id){
        if (StringUtils.isEmpty(id)){
            ExceptionCast.cast(CommonCode.SERVER_ERROR);
        }
        Optional<CmsConfig> optional = cmsConfigRepostry.findById(id);
        CmsConfig cmsConfig =new CmsConfig();
        if (optional.isPresent()){
             cmsConfig = optional.get();
        }
       return cmsConfig;
    }
}
