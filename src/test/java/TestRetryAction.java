import com.fluentest.BaseTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestRetryAction extends BaseTestCase {

    @Override
    @Test(expectedExceptions = com.fluentest.TestFailureException.class, expectedExceptionsMessageRegExp = "the retry action \\[TestRetryAction:\\d+\\] is failed for 5 time\\(s\\): java.lang.AssertionError: error")
    public void runTest() {
        super.runTest();
    }

    @Override
    public void runTestProcedure() {
        step("step",
                retry(5,
                        action(ctx -> {
                            Assert.fail("error");
                        })
                )
        );
    }
}
