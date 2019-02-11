import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicLong;

import static com.fluentest.TestMethods.delay;
import static com.fluentest.TestMethods.step;

public class TestDelayAction {

    @Test
    public void DelayOneSecond() {
        int delayMillis = 100;
        AtomicLong executionTimestamp = new AtomicLong(0);
        AtomicLong startTimestamp = new AtomicLong(0);
        step("",
                ctx -> {
                    startTimestamp.set(System.currentTimeMillis());
                },
                delay(delayMillis,
                        ctx -> {
                            executionTimestamp.set(System.currentTimeMillis());
                            long diff = executionTimestamp.get() - startTimestamp.get() - delayMillis;
                            if (Math.abs(diff) > 5)
                                Assert.fail(String.format("the delay time (%d) is larger than 5ms", diff));
                        }
                )
        );
    }
}
