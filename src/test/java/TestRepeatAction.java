import com.fluentest.BaseTestCase;
import com.fluentest.util.Logger;

public class TestRepeatAction extends BaseTestCase {

    @Override
    public void runTestProcedure() {
        step("step1",
                repeat(10,
                        action(ctx -> {
                            Logger.info("## %d / %d ##", ctx.getNumberOfRepeats(), ctx.getRepeatTimes());
                        })
                )
        );
    }
}
