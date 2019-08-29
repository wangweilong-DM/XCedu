package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.dao.CmsPageRepostry;
import com.xuecheng.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * 监听Mq，接受页面发布的消息
 * @author Wang
 * @Version 1.0
 * @date 2019/8/28 11:07
 */
@Component
public class ConsumerPostPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);

    @Autowired
    PageService pageService;

    @Autowired
    CmsPageRepostry cmsPageRepostry;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg){
        //解析消息
        Map map = JSON.parseObject(msg, Map.class);
        LOGGER.info("receive cms post page:{}",msg.toString());
        //取出页面id
        String pageId =(String) map.get("pageId");
        //查询页面信息
        Optional<CmsPage> optional = cmsPageRepostry.findById(pageId);
        if (!optional.isPresent()){
            LOGGER.error("receive cms post page,cmsPage is null:{}",msg.toString());
            return;
        }

        //将页面保存到服务器的物理路径
        pageService.savePageToServerPath(pageId);
    }



}
