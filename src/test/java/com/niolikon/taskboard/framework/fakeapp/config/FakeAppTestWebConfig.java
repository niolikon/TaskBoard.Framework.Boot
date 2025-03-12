package com.niolikon.taskboard.framework.fakeapp.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootConfiguration
@ComponentScan(basePackages = {
        "com.niolikon.taskboard.framework.fakeapp.course.controller",
        "com.niolikon.taskboard.framework.fakeapp.course.service"
})
@Import(FakeAppTestSecurityConfig.class)
@EnableWebSecurity
public class FakeAppTestWebConfig {
}

