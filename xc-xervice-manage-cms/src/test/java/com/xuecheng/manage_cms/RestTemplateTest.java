package com.xuecheng.manage_cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Action;
import java.util.Map;

/**
 * @author Wang
 * @date 2019/8/25 20:39
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RestTemplateTest {

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testRestTemplate(){
       /* ResponseEntity<Map> template = restTemplate.
                getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);*/
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);


        Map forObject = restTemplate.getForObject("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        Map body = forEntity.getBody();
        System.out.println(forEntity);
        System.out.println(body);
        System.out.println(body);
    }
}
