package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.dao.CmsConfigRepostry;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import sun.nio.ch.IOUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @author Wang
 * @date 2019/8/25 20:00
 */
@Service
public class ConfigService {

    @Autowired
    CmsConfigRepostry cmsConfigRepostry;

    @Autowired
    PageService pageService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;


    /**
     * 根据id查询cms页面配置
     *
     * @param id
     * @return
     */
    public CmsConfig fingById(String id) {
        if (StringUtils.isEmpty(id)) {
            ExceptionCast.cast(CommonCode.SERVER_ERROR);
        }
        Optional<CmsConfig> optional = cmsConfigRepostry.findById(id);
        CmsConfig cmsConfig = new CmsConfig();
        if (optional.isPresent()) {
            cmsConfig = optional.get();
        }
        return cmsConfig;
    }

    /**
     * 页面静态化方法
     *
     * @param pageId
     * @return 静态化程序获取页面的DataUrl
     * <p>
     * 静态化程序远程请求DataUrl获取数据模型。
     * <p>
     * 静态化程序获取页面的模板信息
     * <p>
     * 执行页面静态化
     */
    public String getPageHtml(String pageId) {
        Map model = getModelByPageId(pageId);
        if (model == null) {
            //数据模型获取不到
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }

        //获取模板内容
        String template = getTemplateByPageId(pageId);
        if (template == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        //执行页面静态化
        String html = generationHtml(template, model);
        return html;
    }

    //获取数据模型
    private Map getModelByPageId(String pageId) {
        //取出数据模型
        CmsPage cmsPage = pageService.getById(pageId);
        if (StringUtils.isEmpty(cmsPage)) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //取出页面的dataurl
        String dataUrl = cmsPage.getDataUrl();
        //如果dataUrl为空，抛异常
        if (StringUtils.isEmpty(dataUrl)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //通过restTemplate请求dataUrl获取数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }

    //获取页面的模板
    private String getTemplateByPageId(String pageId) {

        CmsPage cmsPage = pageService.getById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //查询模板信息
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        CmsTemplate cmsTemplate = new CmsTemplate();
        if (optional.isPresent()) {
            cmsTemplate = optional.get();
        }
        //获取模板文件id
        String templateFileId = cmsTemplate.getTemplateFileId();

        //从GridFS中取模板文件内容
        //根据文件id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
        //打开一个下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建GridFsResource对象，在获取
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        //从流中获取数据
        try {
            String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //执行静态化
    private String generationHtml(String template, Map model) {

        //创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", template);
        //向configuration配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板
        try {
            Template template1 = configuration.getTemplate("template");
            //调用Api进行静态化
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template1, model);
            return content;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
