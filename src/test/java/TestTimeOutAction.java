import com.fluentest.TestFailureException;
import com.fluentest.util.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.fluentest.TestMethods.*;

public class TestTimeOutAction {

    @Test
    public void testActionWithinTimeout() {
        AtomicBoolean executed = new AtomicBoolean(false);
        timeout(1000, action(ctx -> {
            executed.set(true);
        })).run(null);
        Assert.assertTrue(executed.get());
    }

    @Test(expectedExceptions = TestFailureException.class, expectedExceptionsMessageRegExp = "the action \\[.+\\] is timed out for 100 ms")
    public void testActionTimeout() {
        step("",
                timeout(100, action(ctx -> {
                    sleep(500);
                }))
        );
    }
}
