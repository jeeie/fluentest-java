import com.fluentest.BaseTestCase;
import com.fluentest.util.Logger;

public class TestWhenAction extends BaseTestCase {

    @Override
    public void runTestProcedure() {
        int a = 1;
        int b = 2;
        step("step1",
                when(a < b, action(ctx -> Logger.info("a<b"))),
                when(a >= b, action(ctx -> Logger.info("a>=b")))
        );
    }
}
