package com.xuecheng.framework.domain.cms;

import com.xuecheng.framework.domain.system.SysDictionaryValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/9/3 8:44
 */

public class SysDictionary {
    @Id
    private String id;
    @Field("d_name")
    private String dName;
    @Field("d_type")
    private String dType;
    @Field("d_value")
    private List<SysDictionaryValue> dValue;
}
