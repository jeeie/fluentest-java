package com.fluentest;

import java.util.Arrays;
import java.util.List;

public class TestStep implements ITest {

    private final String description;
    private final String testEntryPoint;
    private final ITestAction[] testActions;


    public TestStep(String description, String testEntryPoint, ITestAction... testActions) {
        this.description = description;
        this.testActions = testActions;
        this.testEntryPoint = testEntryPoint;
    }

    @Override
    public String getName() {
        return "Step";
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getEntryPoint() {
        return testEntryPoint;
    }

    public List<ITestAction> getTestActions() {
        return Arrays.asList(testActions);
    }

}
