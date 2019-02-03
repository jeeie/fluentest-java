import com.fluentest.ExecutionStatus;
import com.fluentest.TestExecutor;
import com.fluentest.util.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

import static com.fluentest.TestMethods.*;

public class TestRetryActionWithExpireTime {

    @Test
    public void testRetryActionWithNoError() {
        AtomicInteger i = new AtomicInteger(0);
        int retryTimes = 3;
        step("the action should execute just 1 time",
                retry(retryTimes, Instant.now().plusSeconds(5),
                        action(ctx -> {
                            i.getAndIncrement();
                        })
                )
        );
        Assert.assertEquals(i.get(), 1);
    }


    @Test(expectedExceptions = com.fluentest.TestFailureException.class,
            expectedExceptionsMessageRegExp = "the retry action \\[TestRetryActionWithExpireTime:\\d+\\] is expired by .*")
    public void testRetryActionExpired() {
        AtomicInteger i = new AtomicInteger(1);
        int retryTimes = 5;
        try {
            step("the third retry will be failed due to expired",
                    retry(retryTimes, Instant.now().plusSeconds(1),
                            action(ctx -> {
                                sleep(400);
                                fail("%d fail(s)", i.getAndIncrement());
                            })
                    )
            );
        } catch (Throwable e) {
            Assert.assertEquals(i.get(), 4);
            throw e;
        }
    }

    @Test
    public void testRetryActionNotExpired() {
        AtomicInteger i = new AtomicInteger(1);
        int retryTimes = 3;
        TestExecutor executor =
                step("the last retry will be succeed",
                        retry(retryTimes, Instant.now().plusSeconds(1),
                                action(ctx -> {
                                    if (i.get() < retryTimes) {
                                        fail("%d fail(s)", i.getAndIncrement());
                                    } else {
                                        Logger.info("Success after %d retries", i.get());
                                    }
                                })
                        )
                );
        Assert.assertEquals(i.get(), retryTimes);
        Assert.assertEquals(executor.getStatus(), ExecutionStatus.Success);
    }

    @Test(expectedExceptions = com.fluentest.TestFailureException.class,
            expectedExceptionsMessageRegExp = "the retry action \\[TestRetryActionWithExpireTime:\\d+\\] is expired by .*")
    public void testRetryActionOnlyExpireTime() {
        AtomicInteger i = new AtomicInteger(1);
        try {
            step("if the retry times is not specified, it will keep retry until time expired",
                    retry(Instant.now().plusSeconds(1),
                            action(ctx -> {
                                sleep(400);
                                fail("%d fail(s)", i.getAndIncrement());
                            })
                    )
            );
        } catch (Throwable e) {
            Assert.assertEquals(i.get(), 4);
            throw e;
        }
    }

    @Test(expectedExceptions = com.fluentest.TestFailureException.class,
            expectedExceptionsMessageRegExp = "retry times could not be less than 1 if expire time not specified")
    public void testRetryTimesLessThanOneWithoutExpireTime() {
        AtomicInteger i = new AtomicInteger(1);
        try {
            step("retry times could not be less than 1 if expire time not specified",
                    retry(0, action(ctx -> i.getAndIncrement()))
            );
        } catch (Throwable e) {
            Assert.assertEquals(i.get(), 1);
            throw e;
        }
    }
}
