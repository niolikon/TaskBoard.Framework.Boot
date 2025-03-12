package com.niolikon.taskboard.framework.exceptions.config;

import com.niolikon.taskboard.framework.exceptions.ExceptionHandlerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ExceptionHandlerConfig {

    @Bean
    @ConditionalOnProperty(
            name = {
                    "taskboard.exceptions.handler.enabled"
            }
    )
    public ExceptionHandlerProperties exceptionHandlerProperties(Environment environment) {
        return ExceptionHandlerProperties.builder()
                .enabled(environment.getProperty("taskboard.exceptions.handler.enabled", Boolean.class, true))
                .build();
    }
}
