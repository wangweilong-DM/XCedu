package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms_client.dao.CmsPageRepostry;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepostry;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Optional;


/**
 * @author Wang
 * @Version 1.0
 * @date 2019/8/28 9:43
 */
@Service
public class PageService {

    private static final Logger LOGGER =  LoggerFactory.getLogger(PageService.class);

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    CmsPageRepostry cmsPageRepostry;

    @Autowired
    CmsSiteRepostry cmsSiteRepostry;

    //保存html页面到服务器的物理路径
    public void savePageToServerPath(String pageId) {

        //得到html的文件Id
        String fileId = getFileIdByPageId(pageId);
        //1.从gridFS中查询html文件

        InputStream inputStream = this.getByFileId(fileId);
        if (inputStream == null){
            LOGGER.error("getByFileId 里inputstreamI为空 ，htmlFileId:{}",fileId);
            return;
        }

        //得到站点的物理路径
        CmsPage cmsPage = this.getCmsPageByPageId(pageId);
        String siteId = cmsPage.getSiteId();
        CmsSite cmsSite = this.getCmsSiteBySiteId(siteId);
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();
        //得到页面的物理路径
        String pagePhysicalPath = cmsPage.getPagePhysicalPath();
        //完整的路径=站点的物理路径+页面的物理路径+页面名称
        String pagePath = sitePhysicalPath+pagePhysicalPath+cmsPage.getPageName();

        //2.将html文件保存到服务器的物理路径上
        FileOutputStream fileOutputStream = null;
        try {
             fileOutputStream = new FileOutputStream(new File(pagePath));
            IOUtils.copy(inputStream,fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //根据文件id从GridFS中查询文件内容
    public InputStream getByFileId(String fileId){

        //查出文件对象
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //打开下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //定义GridFsResource
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {

        }
        return null;
    }

    //根据pageId查询html文件Id
    private String getFileIdByPageId(String pageId){
        if (StringUtils.isEmpty(pageId)){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = cmsPageRepostry.findByPageId(pageId);
        if (cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String htmlFileId = cmsPage.getHtmlFileId();
        return htmlFileId;
    }

    //根据id查询页面信息
    public CmsPage getCmsPageByPageId(String pageId){
        Optional<CmsPage> optional = cmsPageRepostry.findById(pageId);
        if (optional.isPresent()){
            CmsPage cmsPage = optional.get();
            return cmsPage;
        }
        return null;
    }

    //根据站点id查询站点信息
    public CmsSite getCmsSiteBySiteId(String siteId){
        Optional<CmsSite> optional = cmsSiteRepostry.findById(siteId);
        if (optional.isPresent()){
            CmsSite cmsSite = optional.get();
            return cmsSite;
        }
        return null;
    }
}
