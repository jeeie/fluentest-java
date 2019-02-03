import com.fluentest.BaseTestCase;
import com.fluentest.util.Logger;

import java.util.stream.IntStream;

public class TestOneStepTwoParallelActionPlusSenquentialAction extends BaseTestCase {

    @Override
    public void runTestProcedure() {
        step(
                "Step1: the step contains 2 parallel actions, the numbers printed in the 2 actions will be mixed",
                parallel(
                        action(
                                "the action1 will print number 1 to 9",
                                ctx -> {
                                    IntStream.range(1, 10).forEach(i -> Logger.info("[Action1] %d", i));
                                }
                        ),
                        action(
                                "the action2 will print number 10 to 19",
                                ctx -> {
                                    IntStream.range(10, 20).forEach(i -> Logger.info("[Action2] %d", i));
                                }
                        )
                ),
                ctx -> {
                    IntStream.range(20, 30).forEach(i -> Logger.info("[Action3] %d", i));
                }
        );
    }
}
