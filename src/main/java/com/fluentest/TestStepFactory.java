package com.fluentest;

import java.util.List;

public class TestStepFactory {
    private static TestStepFactory ourInstance = new TestStepFactory();

    public static TestStepFactory getInstance() {
        return ourInstance;
    }

    private TestStepFactory() {
    }

    public TestStepExecutor startTestStep(String description, String entryPoint, List<ITestAction> testActions) {
        TestStep testStep = new TestStep(description, entryPoint, testActions.toArray(new ITestAction[]{}));
        TestStepExecutor testStepExecutor = new TestStepExecutor(testStep);
        TestStepExecutionManager.getInstance().addTestStepExecutor(testStepExecutor);
        testStepExecutor.start();
        return testStepExecutor;
    }

    public static TestStepExecutor start(String description, String entryPoint, List<ITestAction> testActions) {
        return TestStepFactory.getInstance().startTestStep(description, entryPoint, testActions);
    }

}
