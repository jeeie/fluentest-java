package com.fluentest.actions;

import com.fluentest.*;
import com.fluentest.util.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.List;

public class RetryTestAction implements ITestAction, ITest {

    private final int retryTimes;
    private final Instant expireTime;
    private final List<ITestAction> testActions;
    private final String testEntryPoint;
    private final Thread expireCheckThread;

    private boolean retryActionFinished = false;
    private boolean retryActionExpired = false;

    @Override
    public String getName() {
        return "Retry";
    }

    @Override
    public String getDescription() {
        return getName() + "(" + retryTimes + " times" + ")";
    }

    @Override
    public String getEntryPoint() {
        return testEntryPoint;
    }

    @Override
    public void run(ITestContext testContext) {
        try {
            for (long i = 1; i <= retryTimes || retryTimes < 1; i++) {
                if (retryActionExpired) break;
                Throwable cause = null;
                TestActionExecutor executor = null;
                for (ITestAction action : testActions) {
                    if (retryActionExpired) break;
                    executor = new TestActionExecutor(action);
                    executor.start();
                    executor.join();
                    cause = executor.getException();
                    if (cause != null || !executor.isSuccess()) {
                        break;
                    }
                }
                if ((cause != null || !executor.isSuccess()) && (i < retryTimes || retryTimes < 1)) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    try {
                        cause.printStackTrace(pw);
                        Logger.info("%s\r\n%s", cause.getLocalizedMessage(), sw.toString());
                    } finally {
                        try {
                            sw.close();
                            pw.close();
                        } catch (IOException e) {

                        }
                    }
                    Logger.info("the retry action [%s] is failed for %d time(s), retrying...", testEntryPoint, i);
                } else {
                    if (cause != null) {
                        Logger.info("the retry action [%s] is failed for %d time(s)", testEntryPoint, i);
                        TestFailureException.throwException(String.format("the retry action [%s] is failed for %d time(s)", testEntryPoint, i), cause);
                    }
                    if (!executor.isSuccess()) {
                        TestFailureException.raiseException(String.format("the retry action [%s] is failed for %d time(s)", testEntryPoint, i));
                    }
                    break;

                }
            }
        } finally {
            retryActionFinished = true;
            if (retryActionExpired) {
                TestFailureException.raiseException(String.format("the retry action [%s] is expired by %s", testEntryPoint, expireTime.toString()));
            }
        }
    }

    public RetryTestAction(String testEntryPoint, int retryTimes, List<ITestAction> testActions) {
        this(testEntryPoint, retryTimes, null, testActions);
    }

    public RetryTestAction(String testEntryPoint, int retryTimes, Instant expireTime, List<ITestAction> testActions) {
        if (retryTimes < 1 && expireTime == null) {
            TestFailureException.raiseException("retry times could not be less than 1 if expire time not specified");
        }
        this.testEntryPoint = testEntryPoint;
        this.testActions = testActions;
        this.retryTimes = retryTimes;
        this.expireTime = expireTime;
        this.expireCheckThread = new Thread(() -> {
            while (true) {
                if (retryActionFinished) break;
                if (Instant.now().isBefore(expireTime)) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {

                    }
                } else {
                    retryActionExpired = true;
                    break;
                }
            }
        });
        if (expireTime != null) expireCheckThread.start();
    }
}
