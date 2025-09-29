package com.niolikon.taskboard.framework.test.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithIsolatedMongoTestScenario {

    /**
     * Specifies the class that contains the dataset for this test.
     * <p>
     * This class must define a static method that returns a collection of
     * test entities to be persisted before the test execution.
     * </p>
     *
     * @return the class containing the dataset
     */
    Class<?> dataClass();

    /**
     * Specifies the name of the static method within `dataClass` that provides the dataset.
     * <p>
     * By default, this method is expected to be named {@code "getDataset"}.
     * If a different name is used, it must be explicitly specified.
     * </p>
     *
     * @return the method name returning the dataset (default: "getDataset")
     */
    String methodName() default "getDataset";
}
