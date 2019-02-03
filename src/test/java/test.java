import com.fluentest.TestMethods;
import org.testng.annotations.Test;

public class test extends TestMethods {

    @Test
    public void runTestProcedure() {
        int a = 1;
        int b = 2;
        step("step",
                parallel(
                        action(ctx -> {

                        }),
                        retry(5,
                                repeat(10,
                                        action(ctx -> {

                                        })
                                ),
                                when(a < b, ctx -> {

                                })

                        )

                )
        );
    }
}
