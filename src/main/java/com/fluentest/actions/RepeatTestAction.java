package com.fluentest.actions;

import com.fluentest.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class RepeatTestAction implements ITestAction, ITest {

    private final int repeatTimes;
    private final List<ITestAction> testActions;
    private final String testEntryPoint;

    @Override
    public String getName() {
        return "Repeat";
    }

    @Override
    public String getDescription() {
        return getName() + "(" + repeatTimes + " times" + ")";
    }

    @Override
    public String getEntryPoint() {
        return testEntryPoint;
    }

    @Override
    public void run(ITestContext testContext) {
        IntStream.range(0, repeatTimes).<Consumer<? super ITestAction>>mapToObj(i -> action -> {
            TestActionExecutor executor = new TestActionExecutor(action);
//            executor.getTestContext().setRepeatTimes(repeatTimes);
//            executor.getTestContext().setNumberOfRepeats(i + 1);
            executor.start();
            executor.join();
            Throwable cause = executor.getException();
            if (cause != null) {
                TestFailureException.throwException(cause);
            }
            if (!executor.isSuccess()) {
                TestFailureException.raiseException(String.format("the repeat action [%s] is failed", testEntryPoint));
            }
        }).forEach(testActions::forEach);
    }

    public RepeatTestAction(String testEntryPoint, int repeatTimes, List<ITestAction> testActions) {
        this.testEntryPoint = testEntryPoint;
        this.testActions = testActions;
        this.repeatTimes = repeatTimes > 1 ? repeatTimes : 1;
    }
}
