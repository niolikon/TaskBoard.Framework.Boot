package com.niolikon.taskboard.framework.security.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@ComponentScan(basePackages = {
        "com.niolikon.taskboard.framework.security",
})
public class FrameworkSecurityConfig {
}

