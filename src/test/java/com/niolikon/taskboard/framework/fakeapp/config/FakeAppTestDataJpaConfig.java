package com.niolikon.taskboard.framework.fakeapp.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootConfiguration
@ComponentScan(basePackages = {
        "com.niolikon.taskboard.framework.fakeapp.course.repository"
})
@EntityScan(basePackages = "com.niolikon.taskboard.framework.fakeapp.course.model")
@EnableJpaRepositories(basePackages = "com.niolikon.taskboard.framework.fakeapp.course.repository")
@EnableTransactionManagement
public class FakeAppTestDataJpaConfig {

}
