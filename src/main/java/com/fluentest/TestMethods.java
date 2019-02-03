package com.fluentest;

import com.fluentest.actions.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestMethods {

    public static TestStepExecutor step(String description, ITestAction testAction, ITestAction... extraTestActions) {
        List<ITestAction> testActions = new ArrayList<>();
        testActions.add(testAction);
        if (extraTestActions.length > 0) testActions.addAll(Arrays.asList(extraTestActions));
        TestStepExecutor testStepExecutor = TestStepFactory.start(description, getMethodEntryPoint(), testActions);
        testStepExecutor.join();
        Throwable cause = testStepExecutor.getException();
        if (cause != null) {
            TestFailureException.throwException(cause);
        }
        return testStepExecutor;
    }

    public static ITestAction parallel(ITestAction testAction, ITestAction... extraTestActions) {
        List<ITestAction> testActions = new ArrayList<>();
        testActions.add(testAction);
        if (extraTestActions.length > 0) testActions.addAll(Arrays.asList(extraTestActions));
        return new ParallelTestAction(getMethodEntryPoint(), testActions);
    }

    public static ITestAction action(String description, ITestAction testAction) {
        return new BasicTestAction(description, getMethodEntryPoint(), testAction);
    }

    public static ITestAction action(ITestAction testAction) {
        return new BasicTestAction(null, getMethodEntryPoint(), testAction);
    }

    public static ITestAction repeat(int repeatTimes, ITestAction testAction, ITestAction... extraTestActions) {
        List<ITestAction> testActions = new ArrayList<>();
        testActions.add(testAction);
        if (extraTestActions.length > 0) testActions.addAll(Arrays.asList(extraTestActions));
        return new RepeatTestAction(getMethodEntryPoint(), repeatTimes, testActions);
    }

    public static ITestAction retry(int retryTimes, ITestAction testAction, ITestAction... extraTestActions) {
        List<ITestAction> testActions = new ArrayList<>();
        testActions.add(testAction);
        if (extraTestActions.length > 0) testActions.addAll(Arrays.asList(extraTestActions));
        return new RetryTestAction(getMethodEntryPoint(), retryTimes, testActions);
    }

    public static ITestAction retry(Instant expireTime, ITestAction testAction, ITestAction... extraTestActions) {
        List<ITestAction> testActions = new ArrayList<>();
        testActions.add(testAction);
        if (extraTestActions.length > 0) testActions.addAll(Arrays.asList(extraTestActions));
        return new RetryTestAction(getMethodEntryPoint(), 0, expireTime, testActions);
    }

    public static ITestAction retry(int retryTimes, Instant expireTime, ITestAction testAction, ITestAction... extraTestActions) {
        List<ITestAction> testActions = new ArrayList<>();
        testActions.add(testAction);
        if (extraTestActions.length > 0) testActions.addAll(Arrays.asList(extraTestActions));
        return new RetryTestAction(getMethodEntryPoint(), retryTimes, expireTime, testActions);
    }

    public static ITestAction timeout(int timeoutInMilliSeconds, ITestAction testAction, ITestAction... extraTestActions) {
        List<ITestAction> testActions = new ArrayList<>();
        testActions.add(testAction);
        if (extraTestActions.length > 0) testActions.addAll(Arrays.asList(extraTestActions));
        return new TimeoutTestAction(getMethodEntryPoint(), timeoutInMilliSeconds, testActions);
    }

    public static ITestAction when(boolean testResult, ITestAction testAction, ITestAction... extraTestActions) {
        List<ITestAction> testActions = new ArrayList<>();
        testActions.add(testAction);
        if (extraTestActions.length > 0) testActions.addAll(Arrays.asList(extraTestActions));
        return new WhenTestAction(getMethodEntryPoint(), testResult, testActions);
    }

    public static void sleep(int milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
    }

    public static void fail(String message, Object... args) {
        TestFailureException.raiseException(String.format(message, args));
    }

    private static String getMethodEntryPoint() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            StackTraceElement element = elements[i];
            if (element.getMethodName().equals("getMethodEntryPoint")) {
                return elements[i + 2].getClassName() + ":" + elements[i + 2].getLineNumber();
            }
        }
        return "NoDef";
    }

}
