package com.sowhile.registration.msm.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantPropertiesUtils implements InitializingBean {

    public static String REGION_Id;
    public static String ACCESS_KEY_ID;
    public static String SECRECT;
    @Value("${aliyun.sms.regionId}")
    private String regionId;
    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.sms.secret}")
    private String secret;

    @Override
    public void afterPropertiesSet() throws Exception {
        REGION_Id = regionId;
        ACCESS_KEY_ID = accessKeyId;
        SECRECT = secret;
    }
}
