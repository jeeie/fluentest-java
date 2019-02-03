package com.fluentest;

public interface ITestContext<T> {

    void logI(String... message);

    void logE(String... message);

    void logW(String... message);

    int getRepeatTimes();

    int getNumberOfRepeats();

}
