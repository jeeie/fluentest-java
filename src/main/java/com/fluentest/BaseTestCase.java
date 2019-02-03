package com.fluentest;

import com.fluentest.util.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public abstract class BaseTestCase extends TestMethods {

    private ExecutionStatus executionStatus = ExecutionStatus.Init;

    @BeforeTest
    public void prepareTest() {
        initTest();
    }

    protected void initTest() {

    }

    @Test
    public void runTest() {
        Logger.info(this.getClass(), "Starting testcase: %s", this.getClass().getSimpleName());
        executionStatus = ExecutionStatus.Running;
        try {
            this.runTestProcedure();
            executionStatus = ExecutionStatus.Success;
        } catch (Throwable t) {
            executionStatus = ExecutionStatus.Fail;
            throw t;
        } finally {
            Logger.info(this.getClass(), "Stopping testcase: %s", this.getClass().getSimpleName());
        }
    }

    public abstract void runTestProcedure();

    @AfterTest
    public void afterTest() {
        printTestSummary();
        cleanTest();
    }

    protected void printTestSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Test Summary: \n");
        for (TestStepExecutor stepExecutor : TestStepExecutionManager.getInstance().getTestStepExecutors()) {
            sb.append(String.format("Step: %s (%s) - %s \n",
                    stepExecutor.getTestStep().getDescription(),
                    stepExecutor.getStatus().toString(),
                    stepExecutor.getTestStep().getEntryPoint()));
        }
        Logger.info(sb.toString());
    }

    protected void cleanTest() {

    }


}
