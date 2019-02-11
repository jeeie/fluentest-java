package com.fluentest.actions;

import com.fluentest.*;

import java.util.List;

public class DelayTestAction implements ITestAction, ITest {

    private final long delayMilliSeconds;
    private final String entryPoint;
    private final List<ITestAction> testActions;

    @Override
    public String getName() {
        return "Delay";
    }

    @Override
    public String getDescription() {
        return String.format("delay for %d ms", delayMilliSeconds);
    }

    @Override
    public String getEntryPoint() {
        return entryPoint;
    }

    @Override
    public void run(ITestContext testContext) {
        TestMethods.sleep(delayMilliSeconds);
        for (ITestAction testAction : testActions) {
            TestActionExecutor executor = new TestActionExecutor(testAction);
            executor.start();
            executor.join();
            Throwable cause = executor.getException();
            if (cause != null) {
                TestFailureException.throwException(cause);
            }
            if (!executor.isSuccess()) {
                TestFailureException.raiseException(String.format("the when action [%s] is failed", entryPoint));
            }
        }
    }

    public DelayTestAction(String entryPoint, long delayMilliSeconds, List<ITestAction> testActions) {
        this.entryPoint = entryPoint;
        this.delayMilliSeconds = delayMilliSeconds;
        this.testActions = testActions;
    }
}
