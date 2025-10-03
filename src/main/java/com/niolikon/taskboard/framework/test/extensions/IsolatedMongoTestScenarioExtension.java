package com.niolikon.taskboard.framework.test.extensions;

import com.niolikon.taskboard.framework.test.annotations.WithIsolatedMongoTestScenario;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.util.Collection;

public class IsolatedMongoTestScenarioExtension implements BeforeEachCallback, AfterEachCallback {

    private MongoTemplate mongoTemplate;

    @Override
    public void beforeEach(ExtensionContext context) {
        mongoTemplate = SpringExtension.getApplicationContext(context).getBean(MongoTemplate.class);

        mongoTemplate.getDb().drop();

        context.getTestMethod().ifPresent(method -> {
            WithIsolatedMongoTestScenario ann = method.getAnnotation(WithIsolatedMongoTestScenario.class);
            if (ann != null) {
                try {
                    Class<?> dataClass = ann.dataClass();
                    String methodName = ann.methodName();

                    Method datasetMethod = dataClass.getDeclaredMethod(methodName);
                    Object result = datasetMethod.invoke(null);

                    if (result == null) return;

                    if (result instanceof Collection<?> collection) {
                        mongoTemplate.insertAll(collection);
                    } else {
                        throw new IllegalStateException(
                                "Dataset method must return a Collection (e.g., List<?>). Found: " + result.getClass()
                        );
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error loading dataset for test: " + method.getName(), e);
                }
            }
        });
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (mongoTemplate == null) {
            throw new IllegalStateException("MongoTemplate was not initialized!");
        }
        mongoTemplate.getDb().drop();
    }
}
