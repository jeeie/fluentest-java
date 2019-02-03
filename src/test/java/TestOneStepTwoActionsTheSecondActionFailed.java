import com.fluentest.BaseTestCase;
import com.fluentest.TestFailureException;
import com.fluentest.util.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.stream.IntStream;

public class TestOneStepTwoActionsTheSecondActionFailed extends BaseTestCase {

    @Override
    @Test(expectedExceptions = TestFailureException.class, expectedExceptionsMessageRegExp = "java.lang.AssertionError: action2 failed")
    public void runTest() {
        super.runTest();
    }

    @Override
    public void runTestProcedure() {
        step(
                "Step1: the step contains 2 sequential actions",
                action(
                        "the action1 will print number 1 to 9",
                        ctx -> {
                            IntStream.range(1, 10).forEach(i -> Logger.info(String.valueOf(i)));
                        }
                ),
                action(
                        "the action2 will print number 10 to 19",
                        ctx -> {
                            IntStream.range(10, 20).forEach(i -> {
                                Logger.info(String.valueOf(i));
                                if (i == 15) {
                                    Assert.fail("action2 failed");
                                }
                            });
                        }
                )
        );
    }
}
