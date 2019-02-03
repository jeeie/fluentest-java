package com.fluentest.actions;

import com.fluentest.*;

import java.util.List;

public class WhenTestAction implements ITestAction, ITest {

    private final boolean testResult;
    private final List<ITestAction> testActions;
    private final String testEntryPoint;

    @Override
    public String getName() {
        return "When";
    }

    @Override
    public String getDescription() {
        return getName();
    }

    @Override
    public String getEntryPoint() {
        return testEntryPoint;
    }

    @Override
    public void run(ITestContext testContext) {
        if (testResult) {
            for (ITestAction action : testActions) {
                TestActionExecutor executor = new TestActionExecutor(action);
                executor.start();
                executor.join();
                Throwable cause = executor.getException();
                if (cause != null) {
                    TestFailureException.throwException(cause);
                }
                if (!executor.isSuccess()) {
                    TestFailureException.raiseException(String.format("the when action [%s] is failed", testEntryPoint));
                }
            }
        }
    }

    public WhenTestAction(String testEntryPoint, boolean testResult, List<ITestAction> testActions) {
        this.testEntryPoint = testEntryPoint;
        this.testResult = testResult;
        this.testActions = testActions;
    }
}
