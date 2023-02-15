package com.sowhile.registration.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.sowhile.registration.user.mapper")
public class UserConfig {
}
