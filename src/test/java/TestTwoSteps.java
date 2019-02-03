import com.fluentest.BaseTestCase;
import com.fluentest.util.Logger;

public class TestTwoSteps extends BaseTestCase {
    @Override
    public void runTestProcedure() {
        step(
                "step1",
                parallel(
                        ctx -> {
                            Logger.info("action1");
                        }
                )
        );

        step(
                "step2",
                ctx -> {
                    Logger.info("action2");
                }
        );
    }
}
