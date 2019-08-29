package com.xuecheng.manage_cms_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Wang
 * @date 2019/8/28 9:01
 */
@SpringBootApplication
//@EntityScan("com.xuecheng.framework.domain.cms")//扫描实体类
//@ComponentScan(basePackages = {"com.xuecheng.framework"})//扫描common包下面的类
@ComponentScan(basePackages = {"com.xuecheng.manage_cms_client"})//扫描接口
public class ManageCmsClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageCmsClientApplication.class,args);
    }
}
