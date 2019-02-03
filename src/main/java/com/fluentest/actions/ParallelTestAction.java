package com.fluentest.actions;

import com.fluentest.*;

import java.util.ArrayList;
import java.util.List;

public class ParallelTestAction implements ITestAction, ITest {

    private final List<TestActionExecutor> executors = new ArrayList<>();
    private final List<ITestAction> testActions;
    private final String testEntryPoint;

    public ParallelTestAction(String testEntryPoint, List<ITestAction> testActions) {
        this.testEntryPoint = testEntryPoint;
        this.testActions = testActions;
    }

    @Override
    public void run(ITestContext testContext) {

        Object syncLocker = new Object();
        testActions.forEach(
                testAction -> executors.add(new TestActionExecutor(testAction, syncLocker))
        );

        executors.forEach(t -> t.start());

        try {
            while (true) {
                boolean allActionsReady = true;
                for (TestActionExecutor executor : executors) {
                    if (!executor.isStarted()) {
                        allActionsReady = false;
                        break;
                    }
                }
                if (allActionsReady) break;
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            TestFailureException.throwException(e);
        }

        synchronized (syncLocker) {
            syncLocker.notifyAll();
        }

        executors.forEach(t -> t.join());

        for (TestActionExecutor executor : executors) {
            Throwable cause = executor.getException();
            if (cause != null) {
                TestFailureException.throwException(cause);
            }
        }

        for (TestActionExecutor executor : executors) {
            if (executor.isFinished() && !executor.isSuccess()) {
                String actionDescription = null;
                if (executor.getTestAction() instanceof ITest) {
                    actionDescription = ((ITest) executor.getTestAction()).getDescription();
                }
                TestFailureException.raiseException(String.format("the test action [%s] is failed", actionDescription));
            }
        }
    }

    @Override
    public String getName() {
        return "Parallel";
    }

    @Override
    public String getDescription() {
        return "Parallel";
    }

    @Override
    public String getEntryPoint() {
        return testEntryPoint;
    }
}
