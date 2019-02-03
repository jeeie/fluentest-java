package com.fluentest;

import java.util.ArrayList;
import java.util.List;

public class TestStepExecutionManager {
    private static TestStepExecutionManager ourInstance = new TestStepExecutionManager();

    public static TestStepExecutionManager getInstance() {
        return ourInstance;
    }

    private TestStepExecutionManager() {
    }

    private List<TestStepExecutor> testSteps = new ArrayList<>();

    public void addTestStepExecutor(TestStepExecutor testStepExecutor) {
        testSteps.add(testStepExecutor);
    }

    public TestStepExecutor[] getTestStepExecutors() {
        return testSteps.toArray(new TestStepExecutor[]{});
    }

}
