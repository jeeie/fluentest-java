import com.fluentest.util.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.stream.IntStream;

import static com.fluentest.TestMethods.action;
import static com.fluentest.TestMethods.step;

public class TestOneStepOneAction {

    @Test
    public void runTestProcedure() {
        int rangeEndNumber = 4;
        step(
                "Step1: the step contains 1 action",
                action(
                        "the action will print number 1 to 3",
                        ctx -> {
                            IntStream.range(1, rangeEndNumber).forEach(i -> log(i));
                        })
        );
        Assert.assertEquals(i, rangeEndNumber);
    }

    int i = 1;

    private void log(int n) {
        Logger.info("## %d ##", n);
        Assert.assertEquals(n, i++);
    }
}
