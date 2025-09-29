package com.niolikon.taskboard.framework.fakeapp.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootConfiguration
@ComponentScan(basePackages = {
        "com.niolikon.taskboard.framework.fakeapp.documents"
})
@EnableMongoRepositories(basePackages = {
        "com.niolikon.taskboard.framework.fakeapp.documents.repository"
})
public class FakeAppMongoTestConfig {
}
