package com.fluentest.actions;

import com.fluentest.*;
import com.fluentest.util.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.List;

public class TimeoutTestAction implements ITestAction, ITest {

    private final long timeoutMilliSeconds;
    private final Instant expireTime;
    private final List<ITestAction> testActions;
    private final String testEntryPoint;
    private final Thread expireCheckThread;


    private boolean actionFinished = false;
    private boolean actionTimeout = false;

    @Override
    public String getName() {
        return "Timeout";
    }

    @Override
    public String getDescription() {
        return getName() + "(" + timeoutMilliSeconds + "ms" + ")";
    }

    @Override
    public String getEntryPoint() {
        return testEntryPoint;
    }

    @Override
    public void run(ITestContext testContext) {
        try {
            if (!actionTimeout) {
                Throwable cause = null;
                TestActionExecutor executor = null;
                for (ITestAction action : testActions) {
                    if (actionTimeout) break;
                    executor = new TestActionExecutor(action);
                    executor.start();
                    executor.join();
                    cause = executor.getException();
                    if (cause != null || !executor.isSuccess()) {
                        break;
                    }
                }

                if (cause != null) {
                    TestFailureException.throwException(cause);
                }
                if (!executor.isSuccess()) {
                    TestFailureException.raiseException(String.format("the action [%s] is failed", executor.getTestAction()));
                }

            }
        } finally {
            actionFinished = true;
            if (actionTimeout) {
                TestFailureException.raiseException(String.format("the action [%s] is timed out for %d ms", testEntryPoint, timeoutMilliSeconds));
            }
        }
    }

    public TimeoutTestAction(String testEntryPoint, int milliSeconds, List<ITestAction> testActions) {
        timeoutMilliSeconds = milliSeconds;
        this.testEntryPoint = testEntryPoint;
        this.testActions = testActions;
        expireTime = Instant.now().plusMillis(milliSeconds);
        this.expireCheckThread = new Thread(() -> {
            while (true) {
                if (actionFinished) break;
                if (Instant.now().isBefore(expireTime)) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {

                    }
                } else {
                    actionTimeout = true;
                    break;
                }
            }
        });
        if (expireTime != null) expireCheckThread.start();
    }
}
