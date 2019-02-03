package com.fluentest;

import com.fluentest.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class TestStepExecutor extends TestExecutor {

    private final TestStep testStep;
    private final List<TestActionExecutor> executors = new ArrayList<>();

    public TestStepExecutor(TestStep testStep) {
        this.testStep = testStep;
    }

    public synchronized void setStepStatus(ExecutionStatus executionStatus) {
        if (this.executionStatus.equals(executionStatus)) return;
        ExecutionStatus preStatus = this.executionStatus;
        this.executionStatus = executionStatus;
        String description = testStep.getDescription();
        if (description == null || description.isEmpty()) {
            description = "NoName";
        }
        Logger.info(TestStepExecutor.class, "[%s->%s: %s]", preStatus.toString(), executionStatus.toString(), description);
    }

    public TestStep getTestStep() {
        return testStep;
    }

    public TestActionExecutor[] getTestActionExecutors() {
        return executors.toArray(new TestActionExecutor[]{});
    }

    public synchronized void start() {
        start(this.testStep.getName() + "-" + this.testStep.getEntryPoint());
    }

    @Override
    public void run() {
        this.run(ExecutionOrder.Sequential);
    }

    private void run(ExecutionOrder type) {
        runInSequential();
    }

    private void runInSequential() {
        setStepStatus(ExecutionStatus.Running);
        try {
            for (ITestAction testAction : testStep.getTestActions()) {
                TestActionExecutor executor = new TestActionExecutor(testAction);
                executors.add(executor);
                executor.start();
                executor.join();
                if (executor.getException() != null) {
                    TestFailureException.throwException(executor.getException());
                }
            }
        } finally {
            long successCount = executors.stream().filter(executor -> executor.isSuccess()).count();
            if (successCount == executors.size()) {
                setStepStatus(ExecutionStatus.Success);
            } else {
                setStepStatus(ExecutionStatus.Fail);
//                throwException(String.format("the test step [%s] is failed", this.testStep.getDescription()));
            }
        }
    }

}
