package com.fluentest.actions;

import com.fluentest.ITest;
import com.fluentest.ITestAction;
import com.fluentest.ITestContext;

public class BasicTestAction implements ITestAction, ITest {

    private final ITestAction testAction;
    private final String description;
    private final String testEntryPoint;

    public BasicTestAction(String description, String testEntryPoint, ITestAction testAction) {
        this.description = description;
        this.testEntryPoint = testEntryPoint;
        this.testAction = testAction;
    }

    @Override
    public String getName() {
        return "Action";
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getEntryPoint() {
        return testEntryPoint;
    }

    @Override
    public void run(ITestContext testContext) {
        testAction.run(testContext);
    }

}
