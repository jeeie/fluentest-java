package com.fluentest;

import com.fluentest.util.Logger;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TestActionExecutor extends TestExecutor {

    private static final AtomicInteger actionId = new AtomicInteger(0);

    private final ITestAction testAction;
    private final TestContext testContext = new TestContext();
    private final Object syncLocker;
    private final HashMap<ITestAction, String> actionDescriptions = new HashMap<>();

    public TestActionExecutor(ITestAction testAction) {
        this.testAction = testAction;
        this.syncLocker = null;
    }

    public TestActionExecutor(ITestAction testAction, Object syncLocker) {
        this.testAction = testAction;
        this.syncLocker = syncLocker;
    }

    public ITestAction getTestAction() {
        return this.testAction;
    }

    public TestContext getTestContext() {
        return this.testContext;
    }

    public synchronized void setExecutionStatus(ExecutionStatus executionStatus) {
        if (this.executionStatus.equals(executionStatus)) return;
        ExecutionStatus preStatus = this.executionStatus;
        this.executionStatus = executionStatus;
        String description = null;
        if (testAction instanceof ITest) {
            description = ((ITest) testAction).getDescription();
        }
        if (description == null || description.isEmpty()) {
            description = "NoName";
        }
        Logger.info(TestActionExecutor.class, "[%s->%s: %s]", preStatus.toString(), executionStatus.toString(), description);
    }

    public void start() {
        String actionName = "Action-" + this.testAction.getClass().getSimpleName();
        if (this.testAction instanceof ITest) {
            actionName = ((ITest) this.testAction).getName() + "-";
            actionName += ((ITest) this.testAction).getEntryPoint();
        }
        start(actionName);
    }

    @Override
    public void run() {
        try {
            if (syncLocker != null) {
                synchronized (syncLocker) {
                    setExecutionStatus(ExecutionStatus.Ready);
                    syncLocker.wait();
                }
            }
            setExecutionStatus(ExecutionStatus.Running);
            testAction.run(testContext);
            setExecutionStatus(ExecutionStatus.Success);
        } catch (Throwable cause) {
            setExecutionStatus(ExecutionStatus.Fail);
            TestFailureException.throwException(cause);
        }
    }

    private String getActionDescription(ITestAction testAction) {
        if (actionDescriptions.containsKey(testAction)) {
            return actionDescriptions.get(testAction);
        } else {
            String desc = null;
            if (testAction instanceof ITest) {
                desc = ((ITest) testAction).getDescription();
            }
            if (desc == null || desc.isEmpty()) {
                desc = "Anonymous-" + actionId.incrementAndGet();
                actionDescriptions.put(testAction, desc);
            }
            return desc;
        }
    }

}
