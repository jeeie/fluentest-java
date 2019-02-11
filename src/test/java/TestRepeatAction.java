import com.fluentest.BaseTestCase;
import com.fluentest.util.Logger;
import org.testng.Assert;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TestRepeatAction extends BaseTestCase {

    @Override
    public void runTestProcedure() {
        int repeatCount = 10;
        AtomicInteger i = new AtomicInteger(1);
        step("step1",
                repeat(repeatCount,
                        action(ctx -> {
                            ctx.logI("## %d / %d ##", i.getAndIncrement(), repeatCount);
                        })
                )
        );
        Assert.assertEquals(i.get(), 11);
    }
}
